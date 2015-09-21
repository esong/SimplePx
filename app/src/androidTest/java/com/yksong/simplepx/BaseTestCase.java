package com.yksong.simplepx;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;

/**
 * Created by esong on 15-09-20.
 */
public class BaseTestCase<T extends Activity>  extends ActivityInstrumentationTestCase2 {
    protected T mActivity;

    public BaseTestCase(Class<T> activityClass) {
        super(activityClass);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = (T) getActivity();
    }
}
