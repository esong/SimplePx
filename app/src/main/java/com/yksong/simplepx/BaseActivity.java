package com.yksong.simplepx;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

/**
 * Created by esong on 15-09-12.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideStatusBar();
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
