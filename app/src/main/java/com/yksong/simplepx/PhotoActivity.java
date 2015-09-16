package com.yksong.simplepx;

import android.annotation.TargetApi;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yksong.simplepx.app.PxPreference;
import com.yksong.simplepx.view.PhotoPagerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by esong on 15-09-10.
 */
public class PhotoActivity extends BaseActivity {
    public static final String PHOTO_POSITIION = "PHOTO_POSITIION";

    public static final int POSITION_REQUEST_CODE = 1;
    public static final int POSITION_RESPONSE_CODE = 200;

    @Inject SharedPreferences mPreferences;

    @Bind(R.id.toolbar) Toolbar mToolBar;
    @Bind(R.id.pagerView) PhotoPagerView mPagerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAppComponent().inject(this);

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

    @Override
    public void onBackPressed() {
        returnToMain();
    }

    private void returnToMain() {
        setResult(POSITION_RESPONSE_CODE,
                new Intent().putExtra(PHOTO_POSITIION, mPagerView.getCurrentPosition()));

        boolean fullTransitionEnabled = mPreferences.getBoolean(
                PxPreference.SETTING_DEBUG_FULL_TRANSITION,
                PxPreference.SETTING_DEBUG_FULL_TRANSITION_DEFAULT);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && fullTransitionEnabled) {
            mPagerView.updateTransitionView();
            finishAfterTransition();
        } else {
            finish();
        }
    }
}
