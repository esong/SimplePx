package com.yksong.simplepx.component;

import android.app.Application;

import com.yksong.simplepx.R;
import com.yksong.simplepx.network.PxApi;
import com.yksong.simplepx.network.SignedOkClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Created by esong on 15-09-09.
 */
@Module
public class AppModule {
    private final PxApi mApi;
    private static String HOST = "https://api.500px.com/v1";

    public AppModule(Application application) {
        String consumerKey = application.getResources().getString(R.string.pxConsumerKey);
        String consumerSecret = application.getResources().getString(R.string.pxConsumerSecret);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(HOST)
                .setClient(new SignedOkClient(consumerKey, consumerSecret))
                .build();

        mApi = restAdapter.create(PxApi.class);
    }

    @Provides
    @Singleton
    public PxApi providePxApi() {
        return mApi;
    }
}
