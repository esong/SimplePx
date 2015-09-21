package com.yksong.simplepx.util;

import android.support.test.espresso.IdlingResource;
import android.view.ViewGroup;

/**
 * Created by esong on 15-09-20.
 */
public class ViewResources implements IdlingResource {
    private ResourceCallback mCallback;
    private ViewGroup mViewGroup;

    public ViewResources(ViewGroup views) {
        mViewGroup = views;
    }

    @Override
    public String getName() {
        return ViewResources.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        boolean isIdleNow = mViewGroup.getChildAt(1) != null;
        if (isIdleNow && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
        return isIdleNow;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }
}
