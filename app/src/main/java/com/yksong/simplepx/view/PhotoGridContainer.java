package com.yksong.simplepx.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
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
import com.yksong.simplepx.MainActivity;
import com.yksong.simplepx.PhotoActivity;
import com.yksong.simplepx.R;
import com.yksong.simplepx.model.PhotoProvider;
import com.yksong.simplepx.presenter.PhotoPresenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by esong on 15-09-09.
 */
public class PhotoGridContainer extends RelativeLayout {
    @Bind(R.id.photoGrid) RecyclerView mPhotoList;

    @Inject PhotoProvider mPhotoProvider;
    @Inject PhotoPresenter mPresenter;

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

        ((MainActivity) context).getComponent().inject(this);
        mImageMargin = (int) context.getResources().getDimension(R.dimen.image_margin);
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

        mPresenter.takeView(this);
        PxAdapter adapter = new PxAdapter(mPhotoProvider);
        mPhotoList.setAdapter(adapter);
        mPhotoProvider.takePhotoGrid(adapter);
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

                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getContext().startActivity(new Intent(getContext(), PhotoActivity.class)
                                .putExtra(PhotoActivity.PHOTO_POSITIION, mPosition));
                    }
                });
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
