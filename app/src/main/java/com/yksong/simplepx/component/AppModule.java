package com.yksong.simplepx.component;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.yksong.simplepx.R;
import com.yksong.simplepx.app.PxApp;
import com.yksong.simplepx.app.PxPreference;
import com.yksong.simplepx.model.PhotoProvider;
import com.yksong.simplepx.network.PxApi;
import com.yksong.simplepx.network.SignedOkClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by esong on 15-09-09.
 */
@Module
public class AppModule {
    private final PxApi mApi;
    private final SharedPreferences mPreferences;
    private final PhotoProvider mPhotoProvider;
    private final PxApp mApp;

    private static String HOST = "https://api.500px.com/v1";

    public AppModule(final PxApp application) {
        mApp = application;

        String consumerKey = application.getResources().getString(R.string.pxConsumerKey);
        String consumerSecret = application.getResources().getString(R.string.pxConsumerSecret);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(HOST)
                .setClient(new SignedOkClient(consumerKey, consumerSecret))
                .build();

        mApi = restAdapter.create(PxApi.class);

        mPreferences = application.getSharedPreferences(PxPreference.PREFERENCE_NAME,
                Context.MODE_PRIVATE);

        mPhotoProvider = new PhotoProvider(mApp, mApi, mPreferences);
    }

    @Provides
    @Singleton
    public PxApi providePxApi() {
        return mApi;
    }

    @Provides
    @Singleton
    public SharedPreferences providePreferences() {
        return mPreferences;
    }

    @Provides
    @Singleton
    public PhotoProvider providePhotoProvider() {
        return mPhotoProvider;
    }

    @Provides
    @Singleton
    public PxApp provideApplication() {
        return mApp;
    }
}
