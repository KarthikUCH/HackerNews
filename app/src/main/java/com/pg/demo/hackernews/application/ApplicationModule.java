package com.pg.demo.hackernews.application;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by karthikeyan on 22/7/17.
 */

@Module
public class ApplicationModule {

    private Application mApp;

    ApplicationModule(Application app) {
        mApp = app;
    }

    @Provides
    @Singleton
    Context providesApplicationContext(){
        return mApp;
    }
}
