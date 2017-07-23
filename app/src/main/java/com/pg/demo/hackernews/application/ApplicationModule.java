package com.pg.demo.hackernews.application;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    Retrofit providesRestServiceFactory() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder
                .client(httpClient.build())
                .build();
        return retrofit;
    }
}
