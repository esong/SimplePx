package com.yksong.simplepx.sanity;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.yksong.simplepx.BaseTestCase;
import com.yksong.simplepx.MainActivity;
import com.yksong.simplepx.R;
import com.yksong.simplepx.util.ImageIdlingResource;
import com.yksong.simplepx.util.ViewResources;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by esong on 15-09-20.
 */
public class UserFlowTest extends BaseTestCase<MainActivity> {
    RecyclerView mRecyclerView;

    public UserFlowTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mRecyclerView = (RecyclerView) mActivity.findViewById(R.id.photoGrid);
        registerIdlingResources(new ViewResources(mRecyclerView));
    }

    public void testPhotoLoaded() {
        onView(withId(R.id.photoGrid)).check(matches(isDisplayed()));
        ImageView imageView = (ImageView)(mRecyclerView.getChildAt(1).findViewById(R.id.imageGrid));
        registerIdlingResources(new ImageIdlingResource(imageView));
    }

    public void testScrollAndLoading() {
        onView(withId(R.id.photoGrid)).perform(RecyclerViewActions.scrollToPosition(26))
                .check(matches(isCompletelyDisplayed()));
        ImageView imageView = (ImageView)(mRecyclerView.getChildAt(1).findViewById(R.id.imageGrid));
        registerIdlingResources(new ImageIdlingResource(imageView));
        onView(withId(R.id.photoGrid)).perform(RecyclerViewActions.scrollToPosition(55))
                .check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.photoGrid)).perform(RecyclerViewActions.scrollToPosition(90))
                .check(matches(isCompletelyDisplayed()));

    }

    public void testClick() {
        onView(withId(R.id.photoGrid)).perform(RecyclerViewActions.actionOnItemAtPosition(6,
                click()));
        onView(withId(R.id.pager)).perform(swipeLeft());
        onView(withId(R.id.pager)).perform(swipeLeft());
    }

}
