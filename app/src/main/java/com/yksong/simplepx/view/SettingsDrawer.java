package com.yksong.simplepx.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.internal.ScrimInsetsFrameLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.yksong.simplepx.BaseActivity;
import com.yksong.simplepx.BuildConfig;
import com.yksong.simplepx.R;
import com.yksong.simplepx.app.PxPreference;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * Created by esong on 15-09-16.
 */
public class SettingsDrawer extends ScrimInsetsFrameLayout {
    @Bind(R.id.debug_grid) View mDebugGrid;
    @Bind(R.id.debug_full_transition) Switch mFullTransitionSwitch;
    @Bind(R.id.view_spinner) Spinner mViewSpinner;

    @Inject SharedPreferences mPreferences;
    @Inject RxSharedPreferences mRxPreferences;

    Preference<Integer> mViewTypePref;

    public SettingsDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);

        ((BaseActivity) context).getAppComponent().inject(this);

        mViewTypePref = mRxPreferences.getInteger(PxPreference.SETTING_VIEW_TYPE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);

        if (BuildConfig.DEBUG) {
            mDebugGrid.setVisibility(VISIBLE);
            mFullTransitionSwitch.setChecked(
                    mPreferences.getBoolean(PxPreference.SETTING_DEBUG_FULL_TRANSITION,
                            PxPreference.SETTING_DEBUG_FULL_TRANSITION_DEFAULT));
        }

        mViewSpinner.setAdapter(ArrayAdapter.createFromResource(getContext(),
                R.array.view_array, android.R.layout.simple_spinner_item));

        Integer selectedViewType = mViewTypePref.get();
        mViewSpinner.setSelection(selectedViewType == null ?
                PxPreference.SETTING_VIEW_TYPE_DEFAULT : selectedViewType);
    }

    @OnClick(R.id.debug_full_transition)
    public void clickFullTransitionSwitch(View view){
        mPreferences.edit()
                .putBoolean(PxPreference.SETTING_DEBUG_FULL_TRANSITION,((Switch) view).isChecked())
                .apply();
    }

    @OnItemSelected(R.id.view_spinner)
    public void selectViewSpinner(int position) {
        mViewTypePref.set(position);
    }
}
