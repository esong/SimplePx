package com.yksong.simplepx.component;

import com.yksong.simplepx.MainActivity;
import com.yksong.simplepx.presenter.PhotoPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by esong on 15-09-09.
 */
@Module
public class MainActivityModule {
    private MainActivity mActivity;

    public MainActivityModule(MainActivity activity) {
        mActivity = activity;
    }
}
