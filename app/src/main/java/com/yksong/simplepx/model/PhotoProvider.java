package com.yksong.simplepx.model;

import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;

import com.yksong.simplepx.app.PxApp;
import com.yksong.simplepx.app.PxPreference;
import com.yksong.simplepx.network.PxApi;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by esong on 15-09-11.
 */
public class PhotoProvider {
    private HashMap<String, ApiResult> mPhotoCache = new HashMap<>();
    private ApiResult mCurEndpoint;

    private PxApi mPxApi;
    private SharedPreferences mPreference;
    private PxApp mApp;

    private WeakReference<RecyclerView.Adapter> mPhotoAdapter;
    private boolean mRequesting;

    private Action1<Throwable> mErrorAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            mRequesting = false;
            mApp.handleNetworkError(throwable);
        }
    };

    public PhotoProvider(PxApp app, PxApi pxApi, SharedPreferences preferences) {
        mApp = app;
        mPxApi = pxApi;
        mPreference = preferences;

        // Setup endpoint
        mCurEndpoint = new ApiResult(getFeatureString());

        mPhotoCache.put(mCurEndpoint.feature, mCurEndpoint);
    }

    public Photo get(int position) {
        if (position > mCurEndpoint.photos.size() / 2 && !mRequesting) {
            mCurEndpoint.current_page += 1;
            requestNextPage();
        }
        return mCurEndpoint.photos.get(position);
    }

    public int size() {
        return mCurEndpoint.photos.size();
    }

    public void takePhotoGrid(RecyclerView.Adapter ref) {
        mPhotoAdapter = new WeakReference<>(ref);
    }

    public void changeFeature() {
        String newFeature = getFeatureString();

        if (newFeature != mCurEndpoint.feature) {
            ApiResult featureEndpoint = mPhotoCache.get(newFeature);

            if (featureEndpoint != null) {
                mCurEndpoint = featureEndpoint;
                mPhotoAdapter.get().notifyDataSetChanged();
            } else {
                mCurEndpoint = new ApiResult(newFeature);
                init(new Runnable() {
                    @Override
                    public void run() {
                        mPhotoAdapter.get().notifyDataSetChanged();
                    }
                });
            }
        }
    }

    public void init(final Runnable finished) {
        requestAsyncObservable().subscribe(new Action1<ApiResult>() {
            @Override
            public void call(ApiResult apiResult) {
                mRequesting = false;
                mCurEndpoint.photos = apiResult.photos;
                finished.run();
            }
        }, mErrorAction);
    }

    private void requestNextPage() {
        requestAsyncObservable().subscribe(new Action1<ApiResult>() {
            @Override
            public void call(ApiResult apiResult) {
                mRequesting = false;
                int originalPos = mCurEndpoint.photos.size();
                mCurEndpoint.photos.addAll(apiResult.photos);

                if (mPhotoAdapter != null) {
                    mPhotoAdapter.get().notifyItemRangeInserted(originalPos,
                            apiResult.photos.size());
                }
            }
        }, mErrorAction);
    }

    /**
     * Constructs an async observable.
     * @return AsyncObservable
     */
    private Observable<ApiResult> requestAsyncObservable() {
        mRequesting = true;
        return mPxApi.photos(mCurEndpoint.feature, mCurEndpoint.current_page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private String getFeatureString() {
        return mPreference.getString(PxPreference.MENU_PREFERENCE_NAVI_ITEM,
                PxPreference.MENU_PREFERENCE_NAVI_ITEM_DEFAULT);
    }
}
