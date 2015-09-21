package com.yksong.simplepx.regression;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yksong.simplepx.BaseTestCase;
import com.yksong.simplepx.MainActivity;
import com.yksong.simplepx.R;
import com.yksong.simplepx.util.ViewResources;
import com.yksong.simplepx.view.PhotoPagerView;
import com.yksong.simplepx.widget.TouchImageView;

import org.junit.Assert;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.doubleClick;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Most of the bugs on PargerView are related to the InfiniteAdapter.
 * Created by esong on 15-09-21.
 */
public class PagerRegressionTest extends BaseTestCase<MainActivity> {
    public PagerRegressionTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        registerIdlingResources(new ViewResources(
                (RecyclerView) mActivity.findViewById(R.id.photoGrid)));
    }

    /**
     * Crash: Same view used adjacently.
     */
    public void testPagerViewsNotSame() {
        onView(withId(R.id.photoGrid)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
                click()));
        onView(withId(R.id.pager)).perform(swipeRight());
        onView(withId(R.id.pager)).perform(swipeLeft());
        onView(withId(R.id.pager)).perform(swipeRight());
    }

    /**
     * Crash: view not destroyed properly.
     * The number of ViewHolders is set to 6, we need to swipe more than 6 times
     * for a full iteration.
     */
    public void testPagerDestroyed() {
        onView(withId(R.id.photoGrid)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
                click()));

        for (int i = 0; i <= PhotoPagerView.InfinitePagerAdapter.sRealCount; ++i) {
            onView(withId(R.id.pager)).perform(swipeLeft());
        }

        for (int i = 0; i <= PhotoPagerView.InfinitePagerAdapter.sRealCount; ++i) {
            onView(withId(R.id.pager)).perform(swipeRight());
        }
    }

    /**
     * Bug: Views are zoomed without any user interaction.
     * Since views are reused in PagerView, we want each view's zoom to be properly reset.
     */
    public void testViewZoomReset() {
        onView(withId(R.id.photoGrid)).perform(RecyclerViewActions.actionOnItemAtPosition(1,
                click()));

        onView(withId(R.id.pager)).perform(doubleClick());
        for (int i = 0; i < PhotoPagerView.InfinitePagerAdapter.sRealCount; ++i) {
            onView(withId(R.id.pager)).perform(swipeLeft());
        }

        onView(withId(R.id.pager)).check(new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); ++i) {
                    TouchImageView imageView = (TouchImageView) view.findViewById(R.id.imageGrid);
                    Assert.assertEquals(imageView.getCurrentZoom(), 1f, 0);
                }
            }
        });
    }
}
