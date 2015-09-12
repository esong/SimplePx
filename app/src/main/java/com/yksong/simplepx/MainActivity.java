package com.yksong.simplepx;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.yksong.simplepx.app.PxApp;
import com.yksong.simplepx.app.PxPreference;
import com.yksong.simplepx.component.MainActivityComponent;
import com.yksong.simplepx.component.MainActivityModule;
import com.yksong.simplepx.view.PhotoGridContainer;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    private static HashMap<Integer, String> sMenuActionMap = new HashMap<>();

    @Inject SharedPreferences mPreferences;

    @Bind(R.id.toolbar) Toolbar mToolBar;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawer;
    @Bind(R.id.navigation) NavigationView mNavigationView;
    @Bind(R.id.photoGridContainer) PhotoGridContainer mPhotoContainer;

    private MainActivityComponent mComponent;

    static {
        sMenuActionMap.put(R.id.navigation_editor, "editors");
        sMenuActionMap.put(R.id.navigation_fresh, "fresh_today");
        sMenuActionMap.put(R.id.navigation_popular, "popular");
        sMenuActionMap.put(R.id.navigation_upcoming, "upcoming");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mComponent = ((PxApp) getApplication()).getAppComponent()
                .plus(new MainActivityModule(this));
        mComponent.inject(this);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mToolBar.setNavigationIcon(R.drawable.menu_icon);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(GravityCompat.START);
            }
        });

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        String menuAction = sMenuActionMap.get(menuItem.getItemId());
                        if (menuAction != null) {
                            mPreferences.edit()
                                    .putString(PxPreference.MENU_PREFERENCE_NAVI_ITEM, menuAction)
                                    .apply();
                        }

                        return true;
                    }
                });
    }

    public MainActivityComponent getComponent() {
        return mComponent;
    }
}
