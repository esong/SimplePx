package com.yksong.simplepx;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yksong.simplepx.app.PxApp;
import com.yksong.simplepx.component.MainActivityComponent;
import com.yksong.simplepx.component.MainActivityModule;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar) Toolbar mToolBar;

    private MainActivityComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComponent = ((PxApp) getApplication()).getAppComponent()
                .plus(new MainActivityModule(this));
        setContentView(R.layout.activity_main);

        hideStatusBar();
        ButterKnife.bind(this);

        mToolBar.setNavigationIcon(R.drawable.menu_icon);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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

    public MainActivityComponent getComponent() {
        return mComponent;
    }
}
