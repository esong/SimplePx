package com.yksong.simplepx.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by esong on 15-09-13.
 */
public class InfinitePager extends ViewPager {
    public InfinitePager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public class Adapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }
    }
}
