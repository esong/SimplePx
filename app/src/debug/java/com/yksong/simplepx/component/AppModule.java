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
        mApp = application;

        String consumerKey = application.getResources().getString(R.string.pxConsumerKey);
        String consumerSecret = application.getResources().getString(R.string.pxConsumerSecret);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BaseAppModule.HOST)
                .setClient(new SignedOkClient(consumerKey, consumerSecret))
                .build();

        mApi = restAdapter.create(PxApi.class);

        mPreferences = application.getSharedPreferences(PxPreference.PREFERENCE_NAME,
                Context.MODE_PRIVATE);

        mPhotoProvider = new PhotoProvider(mApp, mApi, mPreferences);
    }
}
