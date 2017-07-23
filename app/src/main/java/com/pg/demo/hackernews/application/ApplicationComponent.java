package com.pg.demo.hackernews.application;

import com.pg.demo.hackernews.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by karthikeyan on 22/7/17.
 */

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(HackersNews app);

    void inject(MainActivity activity);

}
