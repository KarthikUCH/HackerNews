package com.pg.demo.hackernews.application;

import android.app.Application;

/**
 * Created by karthikeyan on 22/7/17.
 */

public class HackersNews extends Application {

    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getComponent() {
        return mComponent;
    }
}
