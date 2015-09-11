package com.yksong.simplepx;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.yksong.simplepx.app.PxApp;
import com.yksong.simplepx.model.PhotoProvider;

import javax.inject.Inject;

/**
 * Created by esong on 15-09-11.
 */
public class StartActivity extends Activity {
    @Inject PhotoProvider mPhotoProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        ((PxApp) getApplication()).getAppComponent().inject(this);

        setContentView(R.layout.start_view);
        mPhotoProvider.requestBlocking();

        startActivity(new Intent(this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        int visibility = 0;

        if (Build.VERSION.SDK_INT >= 16) {
            visibility |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        if (Build.VERSION.SDK_INT >= 19) {
            visibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        decorView.setSystemUiVisibility(visibility);
    }
}
