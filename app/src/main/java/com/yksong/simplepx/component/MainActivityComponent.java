package com.yksong.simplepx.component;

import com.yksong.simplepx.MainActivity;
import com.yksong.simplepx.presenter.PhotoPresenter;
import com.yksong.simplepx.view.PhotoGridContainer;

import dagger.Subcomponent;

/**
 * Created by esong on 15-09-09.
 */
@MainActivityScope
@Subcomponent(modules = {MainActivityModule.class})
public interface MainActivityComponent {
    void inject(PhotoGridContainer view);
    void inject(MainActivity activity);
}
