package com.yksong.simplepx.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
        mViewPager.setAdapter(new PagerAdapter() {

            private List<PagerViewHolder> mViewHolders = new ArrayList<>();
            private boolean mInit;

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                if (!mInit) {
                    inflateViews(container);
                }

                PagerViewHolder holder = mViewHolders.get(position % getRealCount());
                Picasso.with(getContext()).load(mPhotoProvider.get(position).image_url)
                        .into(holder.mImageView);

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

            public int getRealCount() {
                return 5;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            private void inflateViews(ViewGroup parent) {
                for (int i = 0; i < getRealCount(); ++i) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.photo_fullscreen, parent, false);
                    mViewHolders.add(new PagerViewHolder(view));
                }

                mInit = true;
            }

            class PagerViewHolder {
                ImageView mImageView;
                View mView;

                public PagerViewHolder(View view) {
                    mImageView = (ImageView) view.findViewById(R.id.imageGrid);
                    mView = view;
                }
            }
        });
        mViewPager.setCurrentItem(mPhotoPosition);
    }

    public int getCurrentPosition() {
        return mPhotoPosition;
    }
}
