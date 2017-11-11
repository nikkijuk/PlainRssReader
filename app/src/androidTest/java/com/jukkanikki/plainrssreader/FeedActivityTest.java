package com.jukkanikki.plainrssreader;

import android.content.pm.ActivityInfo;
import android.os.SystemClock;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Test interactions starting from main FeedActivity
 */
@RunWith(AndroidJUnit4.class)
public class FeedActivityTest {

    @Rule
    public ActivityTestRule<FeedActivity> mActivityRule =
            new ActivityTestRule<>(FeedActivity.class);

    @Test
    public void settingsButtonWorks() {

        // button is visible
        onView(withId(R.id.btnSettings)).check(matches(isDisplayed()));

        // button pressed
        onView(withId(R.id.btnSettings)).perform(click());

        // title of fragement is shown
        onView(withText("PlainRssReader")).check((matches(isDisplayed())));

        // url field is shown
        onView(withText("Url")).check((matches(isDisplayed())));

        // back button pressed
        pressBack();

        // button is visible again
        onView(withId(R.id.btnSettings)).check(matches(isDisplayed()));

        // check that list contains multiple items
        RecyclerView articleView  = (RecyclerView) mActivityRule.getActivity().findViewById(R.id.articleView);
        int itemCount = articleView.getAdapter().getItemCount();
        Assert.assertTrue(itemCount > 0);

        // change orientation: portrait
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        SystemClock.sleep(200);

        // check that list contains multiple items
        RecyclerView articleView2  = (RecyclerView) mActivityRule.getActivity().findViewById(R.id.articleView);
        int itemCount2 = articleView2.getAdapter().getItemCount();
        Assert.assertTrue(itemCount2 > 0);

        // change orientation: portrait
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        SystemClock.sleep(200);

        // check that list contains multiple items
        RecyclerView articleView3  = (RecyclerView) mActivityRule.getActivity().findViewById(R.id.articleView);
        int itemCount3 = articleView3.getAdapter().getItemCount();
        Assert.assertTrue(itemCount3 > 0);

        // now: I'd need to capture screenshots of tests to validate that rotation really happens ..
        // note: one sees on emulator that it happens
        // note: if sleeps are too short this test is not reliable - might be up to speed of processor ..

    }


}
