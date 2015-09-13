package com.yksong.simplepx.component;

import com.yksong.simplepx.StartActivity;
import com.yksong.simplepx.view.PhotoPagerView;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by esong on 15-09-09.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    MainActivityComponent plus(MainActivityModule mainActivityModule);
    void inject(StartActivity activity);
    void inject(PhotoPagerView view);
}
