package com.jukkanikki.plainrssreader;

import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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

    }


}
