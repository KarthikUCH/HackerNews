package com.pg.demo.hackernews.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.pg.demo.hackernews.data.DbConstants.ItemDetail;
import com.pg.demo.hackernews.data.DbConstants.Tables;
import com.pg.demo.hackernews.data.DbConstants.TopStories;
import com.pg.demo.hackernews.network.HackersNewsService;
import com.pg.demo.hackernews.network.models.gson.ResponseStoryItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by karthikeyan on 23/7/17.
 */

public class TopStoryManager {

    private static final String TAG = TopStoryManager.class.getName();
    private static final String COMMA_SEP = ",";

    private final Context mContext;
    private final SQLiteDatabase mDbHelper;
    private final Scheduler mScheduler;
    private final Retrofit mRetrofit;

    private Observer mObserver;
    private boolean flagLoadingStories = false;

    public interface Observer {
        void onStoriesLoaded(ArrayList<ResponseStoryItem> topStories);

        void addStory(ResponseStoryItem story);
    }

    public TopStoryManager(Context context, SQLiteDatabase dbHelper, Scheduler scheduler, Retrofit retrofit) {
        mDbHelper = dbHelper;
        mContext = context;
        mScheduler = scheduler;
        mRetrofit = retrofit;
    }

    public void attach(Observer observer) {
        mObserver = observer;
        Observable.defer(() -> Observable.just(getTopStories()))
                .subscribeOn(mScheduler)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            Log.i(TAG, "OnNext");
                            if (mObserver != null)
                                mObserver.onStoriesLoaded(result);
                        },
                        throwable -> Log.e(TAG, "OnError", throwable),
                        () -> {
                            Log.i(TAG, "OnCompleted");
                            retrieveTopStories();
                        });
    }

    public void detach() {
        mObserver = null;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     DATABASE CALL                                           //
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Get the TopStory list from database
     *
     * @return The list of {@link ResponseStoryItem}
     */
    private ArrayList<ResponseStoryItem> getTopStories() {

        ArrayList<ResponseStoryItem> topStories = new ArrayList<>();

        String QUERY = "SELECT " + ItemDetail.COLUMN_ITEM_ID + COMMA_SEP + ItemDetail.COLUMN_ITEM_PARENT + COMMA_SEP +
                ItemDetail.COLUMN_ITEM_POLL + COMMA_SEP + ItemDetail.COLUMN_ITEM_POST_BY + COMMA_SEP +
                ItemDetail.COLUMN_ITEM_POSTED_TIME + COMMA_SEP + ItemDetail.COLUMN_ITEM_SCORE + COMMA_SEP +
                ItemDetail.COLUMN_ITEM_TEXT + COMMA_SEP + ItemDetail.COLUMN_ITEM_TITLE + COMMA_SEP +
                ItemDetail.COLUMN_ITEM_TYPE + COMMA_SEP + ItemDetail.COLUMN_ITEM_URL + COMMA_SEP + ItemDetail.COLUMN_ITEM_COMMENT_COUNT + " FROM " +
                Tables.TOP_STORIES + " LEFT JOIN " + Tables.ITEM_DETAIL + " ON " +
                Tables.TOP_STORIES + " . " + TopStories.COLUMN_TOP_STORY_ID + " = " + Tables.ITEM_DETAIL + " . " + ItemDetail.COLUMN_ITEM_ID +
                " WHERE " + ItemDetail.COLUMN_ITEM_ID + " IS NOT NULL";

        Cursor cursor = mDbHelper.rawQuery(QUERY, null);
        while (cursor.moveToNext()) {
            ResponseStoryItem story = CursorUtil.getStory(cursor);
            topStories.add(story);
        }

        cursor.close();

        return topStories;
    }


    /**
     * To insert the story id to {@link Tables#TOP_STORIES}
     *
     * @param storyId Unique Id for Story
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    @WorkerThread
    private long insertTopStoryId(long storyId) {
        ContentValues values = new ContentValues();
        values.put(TopStories.COLUMN_TOP_STORY_ID, storyId);
        return mDbHelper.insert(Tables.TOP_STORIES, null, values);
    }

    /**
     * To insert item details (ie: story / comments) to the {@link Tables#ITEM_DETAIL}
     *
     * @param itemDetails
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    @WorkerThread
    public long insertItemDetails(ResponseStoryItem itemDetails) {
        ContentValues values = new ContentValues();
        values.put(ItemDetail.COLUMN_ITEM_ID, itemDetails.getId());
        values.put(ItemDetail.COLUMN_ITEM_PARENT, itemDetails.getParent());
        values.put(ItemDetail.COLUMN_ITEM_POLL, itemDetails.getPoll());
        values.put(ItemDetail.COLUMN_ITEM_POST_BY, itemDetails.getBy());
        values.put(ItemDetail.COLUMN_ITEM_POSTED_TIME, itemDetails.getTime());
        values.put(ItemDetail.COLUMN_ITEM_SCORE, itemDetails.getScore());
        values.put(ItemDetail.COLUMN_ITEM_TEXT, itemDetails.getText());
        values.put(ItemDetail.COLUMN_ITEM_TITLE, itemDetails.getTitle());
        values.put(ItemDetail.COLUMN_ITEM_TYPE, itemDetails.getType());
        values.put(ItemDetail.COLUMN_ITEM_URL, itemDetails.getUrl());
        values.put(ItemDetail.COLUMN_ITEM_COMMENT_COUNT, itemDetails.getDescendants());

        return mDbHelper.insert(Tables.ITEM_DETAIL, null, values);

    }

    /**
     * To check if the Item details is already loaded to database
     *
     * @param itemId unique id of story / comment
     * @return
     */
    private boolean checkStoryExists(long itemId) {
        boolean result = false;
        Cursor cursor = mDbHelper.query(Tables.ITEM_DETAIL, null, ItemDetail.COLUMN_ITEM_ID + " =?", new String[]{String.valueOf(itemId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            result = true;
        }

        cursor.close();
        return result;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     NETWORK CALL                                           //
    ////////////////////////////////////////////////////////////////////////////////////////////////


    private void retrieveTopStories() {
        if (flagLoadingStories) {
            return;
        }
        flagLoadingStories = true;
        Observable.defer(() -> Observable.from(retrieveTopStoryIds()))
                .map(storyId -> {
                    if (!checkStoryExists(storyId)) {
                        ResponseStoryItem item = retrieveStoryDetail(storyId);
                        if (item != null) {
                            long result = insertItemDetails(item);
                            if (result >= 0) {
                                return item;
                            }
                        }
                    }
                    return null;

                })
                .subscribeOn(mScheduler)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            Log.i(TAG, "OnNext");
                            if (result != null) {
                                if (mObserver != null)
                                    mObserver.addStory(result);
                            }
                        },
                        throwable -> Log.e(TAG, "OnError", throwable),
                        () -> {
                            Log.i(TAG, "OnCompleted");
                            flagLoadingStories = false;
                        }
                );
    }


    /**
     * To retrieve TopStories from the server
     *
     * @return List of TopStories
     */
    @WorkerThread
    private List<Long> retrieveTopStoryIds() {
        HackersNewsService mHackersNewsService = mRetrofit.create(HackersNewsService.class);
        Call<List<Long>> call = mHackersNewsService.getTopStories();

        try {
            Response<List<Long>> response = call.execute();
            if (response.code() == 200) {
                for (Long stories : response.body()) {
                    insertTopStoryId(stories);
                }
                return response.body();
            } else {
                throw new Exception(response.message());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    /**
     * To retrieve the item detail from server
     *
     * @param itemId Unique id for item(ie: story)
     * @return {@link ResponseStoryItem} for the provided id
     */
    @WorkerThread
    public ResponseStoryItem retrieveStoryDetail(Long itemId) {
        HackersNewsService mHackersNewsService = mRetrofit.create(HackersNewsService.class);
        Call<ResponseStoryItem> call = mHackersNewsService.getStoryDetail(itemId + ".json");
        try {
            Response<ResponseStoryItem> response = call.execute();
            if (response.code() == 200) {
                return response.body();
            } else {
                throw new Exception(response.message());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
