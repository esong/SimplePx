package com.yksong.simplepx;

import android.content.Intent;
import android.os.Bundle;

import com.yksong.simplepx.app.PxApp;
import com.yksong.simplepx.model.PhotoProvider;

import javax.inject.Inject;

/**
 * Created by esong on 15-09-11.
 */
public class StartActivity extends BaseActivity {
    @Inject PhotoProvider mPhotoProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((PxApp) getApplication()).getAppComponent().inject(this);

        setContentView(R.layout.start_view);
        mPhotoProvider.init();

        startActivity(new Intent(this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
