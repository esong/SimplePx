package com.yksong.simplepx.component;

import com.yksong.simplepx.app.PxApp;

import dagger.Module;

/**
 * Created by esong on 15-09-09.
 */
@Module
public class AppModule extends BaseAppModule {

    public AppModule(final PxApp application) {
        super(application);
    }
}
