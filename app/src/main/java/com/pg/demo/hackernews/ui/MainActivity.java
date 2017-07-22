package com.pg.demo.hackernews.ui;

import android.os.Bundle;

import com.pg.demo.hackernews.R;
import com.pg.demo.hackernews.application.ApplicationComponent;

public class MainActivity extends InjectableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    void injectComponent(ApplicationComponent component) {
        component.inject(this);
    }
}
