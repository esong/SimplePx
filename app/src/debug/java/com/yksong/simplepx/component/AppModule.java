package com.yksong.simplepx.component;

import android.content.Context;

import com.yksong.simplepx.R;
import com.yksong.simplepx.app.PxApp;
import com.yksong.simplepx.app.PxPreference;
import com.yksong.simplepx.model.PhotoProvider;
import com.yksong.simplepx.network.PxApi;
import com.yksong.simplepx.network.SignedOkClient;

import dagger.Module;
import retrofit.RestAdapter;

/**
 * Created by esong on 15-09-09.
 */
@Module
public class AppModule extends BaseAppModule {
    public AppModule(final PxApp application) {
        super(application);
    }
}
