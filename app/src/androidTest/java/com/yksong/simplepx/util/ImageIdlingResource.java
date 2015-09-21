package com.yksong.simplepx.util;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.espresso.IdlingResource;
import android.widget.ImageView;

/**
 * Created by esong on 15-09-20.
 */
public class ImageIdlingResource implements IdlingResource {
    private ImageView mImageView;
    private ResourceCallback mCallback;

    public ImageIdlingResource(ImageView imageView) {
        mImageView = imageView;
    }

    @Override
    public String getName() {
        return "ImageView";
    }

    @Override
    public boolean isIdleNow() {
        boolean isIdleNow = hasImage();
        if (isIdleNow) {
            mCallback.onTransitionToIdle();
        }
        return hasImage();
    }

    private boolean hasImage() {
        Drawable drawable = mImageView.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable) drawable).getBitmap() != null;
        }

        return hasImage;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }
}
