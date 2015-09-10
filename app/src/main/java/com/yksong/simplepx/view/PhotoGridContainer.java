package com.yksong.simplepx.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.yksong.simplepx.MainActivity;
import com.yksong.simplepx.R;
import com.yksong.simplepx.component.MainActivityComponent;
import com.yksong.simplepx.model.Photo;
import com.yksong.simplepx.presenter.PhotoPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by esong on 15-09-09.
 */
public class PhotoGridContainer extends RelativeLayout {
    @Bind(R.id.photoGrid) RecyclerView mPhotoList;

    @Inject PhotoPresenter mPresenter;

    public PhotoGridContainer(Context context) {
        this(context, null);
    }

    public PhotoGridContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoGridContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ((MainActivity) context).getComponent().inject(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        mPhotoList.setLayoutManager(layoutManager);

        mPresenter.takeView(this);
        mPresenter.requestPhotos();
    }

    public void takePhotos(List<Photo> photos) {
        mPhotoList.setAdapter(new PxAdapter(photos));
    }

    public class PxAdapter extends RecyclerView.Adapter<PxAdapter.ViewHolder> {
        List<Photo> mPhotos;
        Picasso mPicasso;

        public PxAdapter(List<Photo> photos) {
            mPhotos = photos;
            mPicasso = Picasso.with(getContext());
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.imageGrid) ImageView mImageView;

            public ViewHolder(View itemView) {
                super(itemView);

                ButterKnife.bind(this, itemView);
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
            mPicasso.load(mPhotos.get(position).image_url)
                    .into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mPhotos.size();
        }
    }

}
