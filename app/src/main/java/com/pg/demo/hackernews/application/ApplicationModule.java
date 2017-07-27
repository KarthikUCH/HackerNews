package com.pg.demo.hackernews.application;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.pg.demo.hackernews.data.DbManager;
import com.pg.demo.hackernews.data.StoryDetailsManager;
import com.pg.demo.hackernews.data.TopStoryManager;
import com.pg.demo.hackernews.network.RestServiceFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by karthikeyan on 22/7/17.
 */

@Module
public class ApplicationModule {

    private static final String API_BASE_URL = "https://hacker-news.firebaseio.com/v0/";
    private Application mApp;

    ApplicationModule(Application app) {
        mApp = app;
    }

    @Provides
    @Singleton
    Context providesApplicationContext() {
        return mApp;
    }

    @Provides
    @Singleton
    RestServiceFactory providesRestServiceFactory() {
        return new RestServiceFactory.Impl();
    }

    @Provides
    SQLiteDatabase providesSQSqLiteDatabase() {
        return DbManager.getInstance(mApp).getDbHelper();
    }

    @Provides
    @Singleton
    Scheduler provideScheduler() {
        return Schedulers.io();
    }

    @Provides
    @Singleton
    TopStoryManager providesTopStoryManager(SQLiteDatabase dbHelper, Scheduler scheduler, RestServiceFactory restServiceFactory) {
        return new TopStoryManager(mApp, dbHelper, scheduler, restServiceFactory);
    }

    @Provides
    @Singleton
    StoryDetailsManager providesStoryDetailsManager(SQLiteDatabase dbHelper, Scheduler scheduler, TopStoryManager topStoryManager) {
        return new StoryDetailsManager(mApp, dbHelper, scheduler, topStoryManager);
    }
}
