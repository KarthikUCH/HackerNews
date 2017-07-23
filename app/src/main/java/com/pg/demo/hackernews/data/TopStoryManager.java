package com.pg.demo.hackernews.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.pg.demo.hackernews.data.DbConstants.*;
import com.pg.demo.hackernews.network.HackersNewsService;
import com.pg.demo.hackernews.network.models.StoryItem;

import java.io.IOException;
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
    private final Context mContext;
    private final SQLiteDatabase mDbHelper;
    private final Scheduler mScheduler;
    private final Retrofit mRetrofit;

    public TopStoryManager(Context context, SQLiteDatabase dbHelper, Scheduler scheduler, Retrofit retrofit) {
        mDbHelper = dbHelper;
        mContext = context;
        mScheduler = scheduler;
        mRetrofit = retrofit;
    }


    private void getTopStories() {
        Observable.defer(() -> Observable.from(retrieveTopStoryIds()))
                .map(id -> retrieveStoryDetail(id))
                .subscribeOn(mScheduler)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            Log.i(TAG, "onNext");
                            if (result != null) {
                                Log.i(TAG, "onSuccess " + result.getTitle());
                            }
                        },
                        throwable -> Log.e(TAG, "onError", throwable),
                        () -> Log.i(TAG, "onCompleted")
                );
    }

    @WorkerThread
    private List<Long> retrieveTopStoryIds() {
        HackersNewsService mHackersNewsService = mRetrofit.create(HackersNewsService.class);
        Call<List<Long>> call = mHackersNewsService.getTopStories();

        try {
            Response<List<Long>> response = call.execute();
            for (Long stories : response.body()) {
                insertTopStoryId(stories);
            }
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private StoryItem retrieveStoryDetail(Long id) {
        HackersNewsService mHackersNewsService = mRetrofit.create(HackersNewsService.class);
        Call<StoryItem> call = mHackersNewsService.getStoryDetail(id + ".json");
        try {
            Response<StoryItem> storyDetail = call.execute();
            return storyDetail.body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private long insertTopStoryId(long id) {
        ContentValues values = new ContentValues();
        values.put(TopStories.COLUMN_TOP_STORY_ID, id);
        return mDbHelper.insert(Tables.TOP_STORIES, null, values);
    }
}
