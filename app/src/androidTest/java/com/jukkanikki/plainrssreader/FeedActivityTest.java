package com.jukkanikki.plainrssreader;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class FeedActivityTest {

    @Rule
    public ActivityTestRule<FeedActivity> mActivityRule =
            new ActivityTestRule<>(FeedActivity.class);

    @Test
    public void ensureTextChangesWork() {
        onView(withId(R.id.btnSettings)).perform(click());
    }


}
