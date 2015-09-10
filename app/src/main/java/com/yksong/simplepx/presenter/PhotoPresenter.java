package com.yksong.simplepx.presenter;

import com.yksong.simplepx.MainActivity;
import com.yksong.simplepx.component.MainActivityScope;
import com.yksong.simplepx.model.ApiResult;
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

    PhotoGridContainer mGridView;

    @Inject
    public PhotoPresenter(){}

    public void takeView(PhotoGridContainer view) {
        mGridView = view;
    }

    public void requestPhotos() {
        mPxApi.photos("editors").subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResult>() {
                    @Override
                    public void call(ApiResult apiResult) {
                        mGridView.takePhotos(apiResult.photos);
                    }
                });
    }
}
