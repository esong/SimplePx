package com.yksong.simplepx.component;

import com.yksong.simplepx.MainActivity;
import com.yksong.simplepx.StartActivity;
import com.yksong.simplepx.view.PhotoGridContainer;
import com.yksong.simplepx.view.PhotoPagerView;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by esong on 15-09-09.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(StartActivity activity);
    void inject(MainActivity activity);

    void inject(PhotoPagerView view);
    void inject(PhotoGridContainer view);
}
