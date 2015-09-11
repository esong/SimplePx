package com.yksong.simplepx.presenter;

import android.content.SharedPreferences;

import com.yksong.simplepx.app.PxPreference;
import com.yksong.simplepx.component.MainActivityScope;
import com.yksong.simplepx.model.ApiResult;
import com.yksong.simplepx.model.PhotoProvider;
import com.yksong.simplepx.network.PxApi;
import com.yksong.simplepx.view.PhotoGridContainer;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by esong on 15-09-09.
 */
@MainActivityScope
public class PhotoPresenter {
    @Inject PxApi mPxApi;
    @Inject SharedPreferences mPreference;
    @Inject PhotoProvider mPhotoProvider;

    PhotoGridContainer mGridView;

    @Inject
    public PhotoPresenter(){}

    public void takeView(PhotoGridContainer view) {
        mGridView = view;
    }

    public void requestPhotos() {
        String feature = mPreference.getString(PxPreference.MENU_PREFERENCE_NAVI_ITEM,
                PxPreference.MENU_PREFERENCE_NAVI_ITEM_DEFAULT);

        mPxApi.photos(feature, 0).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResult>() {
                    @Override
                    public void call(ApiResult apiResult) {
                        mGridView.takePhotos(apiResult.photos);
                    }
                });
    }
}
