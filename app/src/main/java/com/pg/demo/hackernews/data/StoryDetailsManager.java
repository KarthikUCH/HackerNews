package com.pg.demo.hackernews.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pg.demo.hackernews.network.models.StoryDetail;
import com.pg.demo.hackernews.network.models.gson.ResponseStoryItem;

import java.util.ArrayList;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by karthikeyan on 26/7/17.
 */

public class StoryDetailsManager {
    private static final String TAG = StoryDetailsManager.class.getName();

    private final Context mContext;
    private final SQLiteDatabase mDbHelper;
    private final Scheduler mScheduler;
    private final TopStoryManager mTopStoryManager;

    private Observer mObserver;
    private Subscription subscription;
    private long mStoryId;

    public interface Observer {
        void onStoriesLoaded(ArrayList<StoryDetail> topStories);
    }

    public StoryDetailsManager(Context context, SQLiteDatabase dbHelper, Scheduler scheduler, TopStoryManager topStoryManager) {
        mDbHelper = dbHelper;
        mContext = context;
        mScheduler = scheduler;
        mTopStoryManager = topStoryManager;
    }

    public void attach(Observer detailObserver, long storyId) {
        mObserver = detailObserver;
        mStoryId = storyId;
        Observable.defer(() -> Observable.just(getStoryDetails(storyId)))
                .subscribeOn(mScheduler)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            Log.i(TAG, "onNext");
                            if (mObserver != null)
                                mObserver.onStoriesLoaded(result);
                        },
                        throwable -> Log.e(TAG, "onError"),
                        () -> {
                            Log.i(TAG, "On Completed");
                            ArrayList<Long> ids = new ArrayList<>();
                            ids.add(storyId);
                            retrieveStoryComments(ids);
                        });
    }

    public void detach() {
        mObserver = null;
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     DATABASE CALL                                          //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * To get the story and the comments in hierarchy order
     *
     * @param storyId The unique of for the story
     * @return
     */
    private ArrayList<StoryDetail> getStoryDetails(long storyId) {
        ArrayList<StoryDetail> storyDetails = new ArrayList<>();
        String QUERY = " WITH StoryDetail as ( " +
                " SELECT P.item_id, P.item_parent, P.item_poll, P.item_post_by, P.item_posted_time, P.item_score, P.item_text, P.item_title, P.item_type, P.item_url, P.item_comment_count, 1 as level " +
                " FROM item_detail P WHERE P.item_id = ? " +
                " UNION ALL " +
                " SELECT P1.item_id, P1.item_parent, P1.item_poll, P1.item_post_by, P1.item_posted_time, P1.item_score, P1.item_text, P1.item_title, P1.item_type, P1.item_url, P1.item_comment_count, D.level + 1 " +
                " FROM item_detail P1 INNER JOIN StoryDetail D ON D.item_id = P1.item_parent ) " +
                " SELECT * From StoryDetail ";

        Cursor cursor = mDbHelper.rawQuery(QUERY, new String[]{String.valueOf(storyId)});
        while (cursor.moveToNext()) {
            StoryDetail story = CursorUtil.getStoryDetails(cursor);
            storyDetails.add(story);
        }

        return storyDetails;
    }

    private void loadUntilDownloaded() {
        Observable.defer(() -> Observable.just(getStoryDetails(mStoryId)))
                .subscribeOn(mScheduler)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            Log.i(TAG, "OnNext");
                            if (mObserver != null)
                                mObserver.onStoriesLoaded(result);
                        },
                        throwable -> Log.e(TAG, "OnError", throwable),
                        () -> Log.i(TAG, "OnCompleted")
                );
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     NETWORK CALL                                           //
    ////////////////////////////////////////////////////////////////////////////////////////////////

   /* private void retrieveStoryCommentsd(long storyId) {
        Observable.defer(() -> Observable.just(mTopStoryManager.retrieveStoryDetail(storyId).getKids()))
                .map(kidsLst -> {
                    if (kidsLst != null && kidsLst.length > 0) {
                        ArrayList<Long> idLst = new ArrayList<>();
                        for (long id : kidsLst) {
                            idLst.add(id);
                        }
                        retrieveItemKidsDetails(idLst);
                    }
                    return getStoryDetails(storyId);
                })
                .subscribeOn(mScheduler)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            Log.i(TAG, "onNext");
                            if (mObserver != null)
                                mObserver.onStoriesLoaded(result);
                        },
                        throwable -> Log.e(TAG, "onError ", throwable),
                        () -> {
                            Log.i(TAG, "onCompleted");
                        }
                );
    }

    private void retrieveItemKidsDetails(ArrayList<Long> ids) {

        ArrayList<Long> kidsItemList = new ArrayList<>();
        for (long itemId : ids) {
            ResponseStoryItem item = mTopStoryManager.retrieveStoryDetail(itemId);
            mTopStoryManager.insertItemDetails(item);
            if (item.getKids() != null && item.getKids().length > 0) {
                for (long kidsId : item.getKids()) {
                    kidsItemList.add(kidsId);
                }
            }
        }

        if (kidsItemList.size() > 0) {
            retrieveItemKidsDetails(kidsItemList);
        }

    }
}
    */

    private void retrieveStoryComments(ArrayList<Long> kidsIds) {
        subscription = Observable.defer(() -> Observable.just(kidsIds))
                .map(idLst -> {
                    ArrayList<Long> kidsItemList = new ArrayList<>();
                    for (long kidId : idLst) {
                        ResponseStoryItem item = mTopStoryManager.retrieveStoryDetail(kidId);
                        mTopStoryManager.insertItemDetails(item);
                        if (item!= null && item.getKids() != null && item.getKids().length > 0) {
                            for (long kidsId : item.getKids()) {
                                kidsItemList.add(kidsId);
                            }
                        }
                    }
                    return kidsItemList;
                })
                .subscribeOn(mScheduler)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        kidsLst -> {
                            Log.i(TAG, "OnNext");
                            loadUntilDownloaded();
                            if (kidsLst.size() > 0) {
                                retrieveStoryComments(kidsLst);
                            }
                        },
                        throwable -> Log.e(TAG, "OnError", throwable),
                        () -> {
                            Log.i(TAG, "OnCompleted");
                        }
                );

    }


}



