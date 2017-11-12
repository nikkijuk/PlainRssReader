package com.jukkanikki.plainrssreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.jukkanikki.plainrssreader.db.AppDatabase;
import com.jukkanikki.plainrssreader.db.ArticleDao;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagKey;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test interactions starting from main FeedActivity
 *
 * This is grey-box test as it looks inside app's database and view
 * to find out that adapter is loaded with right amount of articles
 */
@RunWith(AndroidJUnit4.class)
public class FeedActivityTest {

    private Context context;

    private AppDatabase db;

    private ArticleDao articleDao;

    @Rule
    public ActivityTestRule<FeedActivity> mActivityRule =
            new ActivityTestRule<>(FeedActivity.class);

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        db = AppDatabase.getDatabase(context); // get db
        articleDao = db.articleModel(); // get dao for articles
    }

    @After
    public void tearDown() {
        db.destroyInstance(); // free db instance from memory
    }


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

        // GREY TEST: find recycler view from app, get adapter, find out amount of articles

        // check that list contains multiple items
        RecyclerView articleView  = (RecyclerView) mActivityRule.getActivity().findViewById(R.id.articleView);
        int itemCount = articleView.getAdapter().getItemCount();
        Assert.assertTrue(itemCount > 0);

        // GREY TEST: find out amount of articles from db, compare to amount of articles in view

        // check that db has same amount of items as adapter
        long dbCount = articleDao.countArticles();
        Assert.assertEquals(dbCount, itemCount);

    }


}
