package com.yksong.simplepx.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.yksong.simplepx.model.PhotoProvider;

import javax.inject.Inject;

/**
 * Created by esong on 15-09-12.
 */
public class PhotoPager extends RelativeLayout {
    @Inject PhotoProvider mPhotoProvider;

    public PhotoPager(Context context, AttributeSet attrs) {
        super(context, attrs);


    }
}
