package com.pg.demo.hackernews.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.pg.demo.hackernews.application.ApplicationComponent;
import com.pg.demo.hackernews.application.HackersNews;

import javax.inject.Inject;

/**
 * Created by karthikeyan on 22/7/17.
 */

public abstract class InjectableActivity extends AppCompatActivity {

    @Inject
    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        injectComponent(((HackersNews) getApplication()).getComponent());
    }

    abstract void injectComponent(ApplicationComponent component);
}
