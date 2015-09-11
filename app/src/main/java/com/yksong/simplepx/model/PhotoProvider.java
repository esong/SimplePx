package com.yksong.simplepx.model;

import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;

import com.yksong.simplepx.app.PxPreference;
import com.yksong.simplepx.network.PxApi;

import java.lang.ref.WeakReference;
import java.util.HashMap;

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

    private WeakReference<RecyclerView.Adapter> mPhotoAdapter;

    public PhotoProvider(PxApi pxApi, SharedPreferences preferences) {
        mPxApi = pxApi;
        mPreference = preferences;

        // Setup endpoint
        mCurEndpoint = new ApiResult(getFeatureString());

        mPhotoCache.put(mCurEndpoint.feature, mCurEndpoint);
    }

    public Photo get(int position) {
        if (position > mCurEndpoint.photos.size() / 2) {
            mCurEndpoint.current_page += 1;
            request();
        }
        return mCurEndpoint.photos.get(position);
    }

    public int size() {
        return mCurEndpoint.photos.size();
    }

    public void takePhotoGrid(RecyclerView.Adapter ref) {
        mPhotoAdapter = new WeakReference<RecyclerView.Adapter>(ref);
    }

    public void request(Action1... actions) {
        String newFeature = getFeatureString();

        if (newFeature != mCurEndpoint.feature) {
            ApiResult featureEndpoint = mPhotoCache.get(newFeature);

            if (featureEndpoint != null) {
                mCurEndpoint = featureEndpoint;
            } else {
                mCurEndpoint = new ApiResult(newFeature);
            }
        }

        request();
    }

    public void request() {
        mPxApi.photos(mCurEndpoint.feature, mCurEndpoint.current_page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResult>() {
                    @Override
                    public void call(ApiResult apiResult) {
                        int originalPos = mCurEndpoint.photos.size();
                        mCurEndpoint.photos.addAll(apiResult.photos);

                        if (mPhotoAdapter != null) {
                            mPhotoAdapter.get().notifyItemRangeInserted(originalPos,
                                    apiResult.photos.size());
                        }
                    }
                });
    }

    public void requestBlocking() {
        if (mCurEndpoint.current_page == 0) {
            mCurEndpoint = mPxApi.photos(mCurEndpoint.feature, mCurEndpoint.current_page)
                    .toBlocking().single();
        }
    }

    private String getFeatureString() {
        return mPreference.getString(PxPreference.MENU_PREFERENCE_NAVI_ITEM,
                PxPreference.MENU_PREFERENCE_NAVI_ITEM_DEFAULT);
    }
}
