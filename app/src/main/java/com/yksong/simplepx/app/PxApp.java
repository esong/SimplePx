package com.yksong.simplepx.app;

import android.app.Application;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.yksong.simplepx.BuildConfig;
import com.yksong.simplepx.R;
import com.yksong.simplepx.component.AppComponent;
import com.yksong.simplepx.component.AppModule;
import com.yksong.simplepx.component.DaggerAppComponent;
import io.fabric.sdk.android.Fabric;

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
        Fabric.with(this, new Crashlytics());
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public void handleNetworkError(Throwable error) {
        Toast.makeText(this, getText(R.string.network_error),
                Toast.LENGTH_LONG).show();

        if (BuildConfig.DEBUG) {
            error.printStackTrace();
        }
    }


    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
