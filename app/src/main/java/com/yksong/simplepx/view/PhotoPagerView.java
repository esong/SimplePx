package com.yksong.simplepx.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.transition.Transition;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yksong.simplepx.PhotoActivity;
import com.yksong.simplepx.R;
import com.yksong.simplepx.app.PxApp;
import com.yksong.simplepx.model.PhotoProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by esong on 15-09-12.
 */
public class PhotoPagerView extends RelativeLayout {
    @Inject PhotoProvider mPhotoProvider;

    @Bind(R.id.pager) ViewPager mViewPager;
    private int mPhotoPosition;
    private boolean mTransitionStarted;

    public PhotoPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        PhotoActivity photoActivity = (PhotoActivity) context;
        ((PxApp)photoActivity.getApplication()).getAppComponent().inject(this);

        mPhotoPosition = photoActivity.getIntent().getIntExtra(PhotoActivity.PHOTO_POSITIION, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);

        mViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.page_margin));
        mViewPager.setAdapter(new InfinitePagerAdapter(mViewPager));
        mViewPager.setCurrentItem(mPhotoPosition);
    }

    public int getCurrentPosition() {
        return mPhotoPosition;
    }

    private class InfinitePagerAdapter extends PagerAdapter {
        static final int sRealCount = 5;
        private List<PagerViewHolder> mViewHolders = new ArrayList<>();

        public InfinitePagerAdapter(ViewPager parent) {
            for (int i = 0; i < sRealCount; ++i) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.photo_fullscreen, parent, false);
                mViewHolders.add(new PagerViewHolder(view));
            }

            ViewCompat.setTransitionName(mViewHolders.get(mPhotoPosition % sRealCount).mView,
                    getResources().getString(R.string.transition_image));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PagerViewHolder holder = mViewHolders.get(position % sRealCount);

            Callback callback = null;

            if (!mTransitionStarted) {
                /**
                 * This callback is used for solve the glitch between EnterTransition, and
                 * ViewPager. Since ViewPager needs an adapter and it also needs to call
                 * instantiateItem, and the adapter/method call all happens after finishInflate.
                 * EnterTransition will be missed.
                 */
                callback =  new Callback() {
                    @Override
                    public void onSuccess() {
                        mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(
                                new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                mViewPager.getViewTreeObserver()
                                        .removeOnGlobalLayoutListener(this);
                                ((PhotoActivity) getContext()).startEnterTransition();
                            }
                        });
                    }
                    @Override
                    public void onError() {
                        ((PhotoActivity) getContext()).startEnterTransition();
                    }
                };

                mTransitionStarted = true;
            }

            Picasso.with(getContext()).load(mPhotoProvider.get(position).image_url)
                    .into(holder.mImageView, callback);

            container.addView(holder.mView);
            mPhotoPosition = position;
            return holder.mView;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        class PagerViewHolder {
            ImageView mImageView;
            View mView;

            public PagerViewHolder(View view) {
                mImageView = (ImageView) view.findViewById(R.id.imageGrid);
                mView = view;
            }
        }
    }

}
