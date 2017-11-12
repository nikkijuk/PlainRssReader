package com.jukkanikki.plainrssreader;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.SystemClock;
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

/**
 * Test interactions starting from main FeedActivity
 *
 * This is grey-box test as it looks inside app's database and view
 * to find out that adapter is loaded with right amount of articles
 */
@RunWith(AndroidJUnit4.class)
public class FeedActivityTest {

    private static final String NYT_SCIENCE_FEED_URL = "http://rss.nytimes.com/services/xml/rss/nyt/Science.xml";

    private static final String NYT_ARTS_FEED_URL = "http://rss.nytimes.com/services/xml/rss/nyt/Arts.xml";

    private static final String PREF_URL_KEY = "rss_source";

    private AppDatabase db;

    private ArticleDao articleDao;

    @Rule
    public ActivityTestRule<FeedActivity> mActivityRule =
            new ActivityTestRule<>(FeedActivity.class);

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getTargetContext();
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

        // --

        //TODO: Here should come changing of url

        // click url
        //onView(withText("Url")).perform(click());
        //onView(withText("Url")).perform(click(), typeTextIntoFocusedView("NYT_ARTS_FEED_URL"));

        // give text
        //onView(withTagKey(PREF_URL_KEY)).perform(typeTextIntoFocusedView("NYT_ARTS_FEED_URL"));

        // press ok
        //onView(withText("OK")).perform(click());

        // --

        // back button pressed
        pressBack();

        // button is visible again
        onView(withId(R.id.btnSettings)).check(matches(isDisplayed()));

        // GREY TEST: find recycler view from app, get adapter, find out amount of articles

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

        // GREY TEST: find out amount of articles from db, compare to amount of articles in view

        // check that db has same amount of items as adapter
        long dbCount = articleDao.countArticles();
        Assert.assertEquals(dbCount, itemCount3);


        // now: I'd need to capture screenshots of tests to validate that rotation really happens ..
        // note: one sees on emulator that it happens
        // note: if sleeps are too short this test is not reliable - might be up to speed of processor ..

    }


}
