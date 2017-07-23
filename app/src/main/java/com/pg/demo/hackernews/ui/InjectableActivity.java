package com.pg.demo.hackernews.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.pg.demo.hackernews.application.ApplicationComponent;
import com.pg.demo.hackernews.application.HackersNews;
import com.pg.demo.hackernews.data.TopStoryManager;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * Created by karthikeyan on 22/7/17.
 */

public abstract class InjectableActivity extends AppCompatActivity {

    @Inject
    Context mContext;

    @Inject
    TopStoryManager mTopStoryManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injectComponent(((HackersNews) getApplication()).getComponent());
    }

    abstract void injectComponent(ApplicationComponent component);
}
