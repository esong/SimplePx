package com.yksong.simplepx.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yksong.simplepx.PhotoActivity;
import com.yksong.simplepx.R;
import com.yksong.simplepx.model.Photo;
import com.yksong.simplepx.model.PhotoProvider;
import com.yksong.simplepx.model.SinglePhotoResult;
import com.yksong.simplepx.network.PxApi;
import com.yksong.simplepx.widget.TouchImageView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by esong on 15-09-12.
 */
public class PhotoPagerView extends RelativeLayout {
    @Inject PhotoProvider mPhotoProvider;
    @Inject PxApi mApi;

    @Bind(R.id.pager) ViewPager mViewPager;
    @BindString(R.string.transition_image) String mTransitionName;
    @BindDimen(R.dimen.page_margin) int mPageMargin;

    private InfinitePagerAdapter mAdapter;
    private int mPhotoPosition;
    private boolean mTransitionStarted;
    private Handler mHandler;

    private static final int UI_ANIMATION_DELAY = 300;

    public PhotoPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        PhotoActivity photoActivity = (PhotoActivity) context;
        photoActivity.getAppComponent().inject(this);

        mPhotoPosition = photoActivity.getIntent().getIntExtra(PhotoActivity.PHOTO_POSITIION, 0);

        mHandler = new Handler();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);

        mViewPager.setPageMargin(mPageMargin);
        mAdapter = new InfinitePagerAdapter(mViewPager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPhotoPosition);
    }

    public int getCurrentPosition() {
        return mPhotoPosition;
    }

    private class InfinitePagerAdapter extends PagerAdapter {
        static final int sRealCount = 6;
        List<PagerViewHolder> mViewHolders = new ArrayList<>();

        private Picasso mPicasso;

        public InfinitePagerAdapter(ViewPager parent) {
            for (int i = 0; i < sRealCount; ++i) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.photo_fullscreen, parent, false);
                mViewHolders.add(new PagerViewHolder(view));
            }

            ViewCompat.setTransitionName(mViewHolders.get(mPhotoPosition % sRealCount).mView,
                    mTransitionName);

            mPicasso = Picasso.with(getContext());
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final PagerViewHolder holder = mViewHolders.get(position % sRealCount);

            final Photo photo = mPhotoProvider.get(position);

            holder.mProgressBar.setVisibility(VISIBLE);

            if (!mTransitionStarted) {
                mTransitionStarted = true;
                // Load cropped first, then load the uncropped photo.
                mPicasso.load(photo.image_url)
                    .into(holder.mImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(
                                new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @Override
                                    /**
                                     * This callback is used for solve the glitch between EnterTransition and
                                     * ViewPager. Since ViewPager needs an adapter and it also needs to call
                                     * instantiateItem, the adapter/method call all happens after finishInflate.
                                     * EnterTransition will be missed.
                                     */
                                    public void onGlobalLayout() {
                                        ((PhotoActivity) getContext()).startEnterTransition();
                                        mViewPager.getViewTreeObserver()
                                                .removeOnGlobalLayoutListener(this);

                                        // Delay loading uncropped photo, wait until transition
                                        // finishes.
                                        mHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                loadUncroppedPhoto(holder, photo.id);
                                            }
                                        }, UI_ANIMATION_DELAY);
                                    }
                                });
                        }

                        @Override
                        public void onError() {
                            ((PhotoActivity) getContext()).startEnterTransition();
                        }
                    });
            } else {
                loadUncroppedPhoto(holder, photo.id);
            }

            container.addView(holder.mView);
            mPhotoPosition = position - 1;
            return holder.mView;
        }


        /*
         * Load uncropped photo from PxApi.
         *
         * The ideal solution is to request multiple image_size from the /photos api;
         * however, after setting image_size[], a consumer key error is returned. Thus,
         * I used this solution.
         */
        private void loadUncroppedPhoto(final PagerViewHolder holder, int photoId) {
            mApi.photoById(photoId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<SinglePhotoResult>() {
                        @Override
                        public void call(SinglePhotoResult apiResult) {
                            if (apiResult.photo != null) {
                                mPicasso.load(apiResult.photo.image_url)
                                    .into(holder.mImageView, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            holder.mProgressBar.setVisibility(GONE);
                                            holder.mImageView.setZoom(1);
                                        }

                                        @Override
                                        public void onError() {
                                        }
                                    });
                            }
                        }
                    });
        }


        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
            TouchImageView imageView = (TouchImageView) view.findViewById(R.id.imageGrid);
            imageView.destroyDrawingCache();
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
            TouchImageView mImageView;
            View mProgressBar;
            View mView;

            public PagerViewHolder(View view) {
                mImageView = (TouchImageView) view.findViewById(R.id.imageGrid);
                mProgressBar = view.findViewById(R.id.progressBar);
                mView = view;
            }
        }
    }

    public void updateTransitionView() {
        for (InfinitePagerAdapter.PagerViewHolder holder : mAdapter.mViewHolders) {
            ViewCompat.setTransitionName(holder.mView, "");
        }

        ViewCompat.setTransitionName(
                mAdapter.mViewHolders.get(mPhotoPosition % InfinitePagerAdapter.sRealCount).mView,
                mTransitionName);
    }

}
