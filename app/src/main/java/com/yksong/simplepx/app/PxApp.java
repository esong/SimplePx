package com.yksong.simplepx.app;

import android.app.Application;

import com.squareup.picasso.Picasso;
import com.yksong.simplepx.component.AppComponent;
import com.yksong.simplepx.component.AppModule;
import com.yksong.simplepx.component.DaggerAppComponent;

/**
 * Created by esong on 15-09-08.
 */
public class PxApp extends Application {
    /**
     * AppComponent stores the global instances.
     */
    AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        Picasso.with(this).setIndicatorsEnabled(true);
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
