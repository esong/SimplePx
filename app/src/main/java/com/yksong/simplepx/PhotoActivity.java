package com.yksong.simplepx;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yksong.simplepx.view.PhotoPagerView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by esong on 15-09-10.
 */
public class PhotoActivity extends BaseActivity {
    public static final String PHOTO_POSITIION = "PHOTO_POSITIION";

    public static final int POSITION_REQUEST_CODE = 1;
    public static final int POSITION_RESPONSE_CODE = 200;

    @Bind(R.id.toolbar) Toolbar mToolBar;
    @Bind(R.id.pagerView) PhotoPagerView mPagerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pauseEnterTransition();
        setContentView(R.layout.photo_pager);

        ButterKnife.bind(this);

        mToolBar.setNavigationIcon(R.drawable.back);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMain();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void pauseEnterTransition() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startEnterTransition() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startPostponedEnterTransition();
        }
    }

    @Override
    public void onBackPressed() {
        returnToMain();
    }

    private void returnToMain() {
        setResult(POSITION_RESPONSE_CODE,
                new Intent().putExtra(PHOTO_POSITIION, mPagerView.getCurrentPosition()));
        finish();
    }
}
