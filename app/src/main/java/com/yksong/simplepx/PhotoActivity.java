package com.yksong.simplepx;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by esong on 15-09-10.
 */
public class PhotoActivity extends BaseActivity {
    public static final String PHOTO_POSITIION = "PHOTO_POSITIION";

    @Bind(R.id.toolbar) Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.photo_pager);
        ButterKnife.bind(this);

        mToolBar.setNavigationIcon(android.R.drawable.ic_delete);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
