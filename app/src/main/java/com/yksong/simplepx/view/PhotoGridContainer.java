package com.yksong.simplepx.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.yksong.simplepx.BaseActivity;
import com.yksong.simplepx.MainActivity;
import com.yksong.simplepx.PhotoActivity;
import com.yksong.simplepx.R;
import com.yksong.simplepx.model.PhotoProvider;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by esong on 15-09-09.
 */
public class PhotoGridContainer extends RelativeLayout {
    @Bind(R.id.photoGrid) RecyclerView mPhotoList;
    @BindString(R.string.transition_image) String mTransitionImage;

    @Inject PhotoProvider mPhotoProvider;

    private GridLayoutManager mLayoutManager;
    private int mImageMargin;

    public PhotoGridContainer(Context context) {
        this(context, null);
    }

    public PhotoGridContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoGridContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mImageMargin = (int) context.getResources().getDimension(R.dimen.image_margin);

        ((BaseActivity) context).getAppComponent().inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);

        mLayoutManager = new GridLayoutManager(getContext(), 3) {

            @Override
            /**
             * Increase layout space for the RecyclerView.
             * This makes RecyclerView rendering 2 screens of photos,
             * which reduces the chance of seeing empty grid.
             */
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return getHeight() * 2;
            }
        };

        mPhotoList.setLayoutManager(mLayoutManager);

        mPhotoList.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    /**
                     * Dynamically calculate the number of spans according to the screen size.
                     */
                    public void onGlobalLayout() {
                        mPhotoList.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int viewWidth = mPhotoList.getMeasuredWidth();
                        float cardViewWidth = getContext().getResources()
                                .getDimension(R.dimen.photo_dimen);
                        int newSpanCount = (int) Math.ceil(viewWidth / cardViewWidth);
                        mLayoutManager.setSpanCount(newSpanCount);
                        mLayoutManager.requestLayout();
                    }
                });

        mPhotoList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view,
                                       RecyclerView parent, RecyclerView.State state) {
                outRect.right = mImageMargin;
                outRect.bottom = mImageMargin;
            }
        });

        PxAdapter adapter = new PxAdapter(mPhotoProvider);
        mPhotoList.setAdapter(adapter);
        mPhotoProvider.takePhotoGrid(adapter);

        // If PhotoProvider is not inited, initialize PhotoProvider under current config.
        if (!mPhotoProvider.isInited()) {
            mPhotoProvider.init(new Runnable() {
                @Override
                public void run() {
                    mPhotoList.invalidate();
                }
            });
        }
    }

    public void moveToPosition(int position) {
        mPhotoList.scrollToPosition(position);
    }

    public void moveToTop() {
        mPhotoList.scrollToPosition(0);
    }

    public class PxAdapter extends RecyclerView.Adapter<PxAdapter.ViewHolder> {
        PhotoProvider mPhotoProvider;
        Picasso mPicasso;

        public PxAdapter(PhotoProvider photoProvider) {
            mPhotoProvider = photoProvider;
            mPicasso = Picasso.with(getContext());
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.imageGrid) ImageView mImageView;
            public int mPosition;

            public ViewHolder(View itemView) {
                super(itemView);

                ButterKnife.bind(this, itemView);
            }

            @OnClick(R.id.squareContainer)
            public void openFullScreenPhoto() {
                MainActivity parentActivity = (MainActivity) getContext();
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        parentActivity,
                        new Pair<View, String>(mImageView, mTransitionImage));

                parentActivity.startActivityForResult(
                        new Intent(getContext(), PhotoActivity.class)
                                .putExtra(PhotoActivity.PHOTO_POSITIION, mPosition),
                        PhotoActivity.POSITION_REQUEST_CODE, options.toBundle());
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.photo_grid, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            mPicasso.load(mPhotoProvider.get(position).image_url)
                    .noFade()
                    .into(holder.mImageView);

            holder.mPosition = position;
        }

        @Override
        public int getItemCount() {
            return mPhotoProvider.size();
        }
    }
}
