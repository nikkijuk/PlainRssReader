package com.jukkanikki.plainrssreader;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jukkanikki.plainrssreader.db.AppDatabase;
import com.jukkanikki.plainrssreader.db.ArticleDao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Test interactions starting from main FeedActivity
 *
 * Testing style is Black Box using UI automator
 *
 * https://developer.android.com/training/testing/ui-testing/uiautomator-testing.html
 *
 * Except that I wanted to try to see inside running app with Espresso,
 * and thus used also ActivityTestRule to find list and count of articles
 * and AppDatabase and ArticleDao to get access to database of running app.
 *
 * I feel this is pretty powerful stuff and allows really strong verification of
 * Apps functionality using both Look&Feel and Internals.
 *
 * Here some starting ideas which I read when writing this test
 *
 * http://alexzh.com/tutorials/android-testing-espresso-uiautomator-together/
 */
@RunWith(AndroidJUnit4.class)
public class FeedActivityUiAutomatorTest {

    private static final String TAG = "FeedUiAutomatorTest";


    /**
     * Espresso rule for FeedActivity
     */
    @Rule
    public ActivityTestRule<FeedActivity> mActivityRule =
            new ActivityTestRule<>(FeedActivity.class);

    /**
     * Apps context
     */
    private Context context;

    /**
     * Database of app
     */
    private AppDatabase db;

    /**
     * Dao of Articles
     */
    private ArticleDao articleDao;


    /**
     * UiAutomators driver for device
     */
    private UiDevice mDevice;

    /**
     * Package of app from AndroidManifest.xml
     */
    private static final String PACKAGE = "com.jukkanikki.plainrssreader";

    /**
     * Wait for app to start
     */
    private static final int LAUNCH_TIMEOUT = 5000;

    /**
     * Wait for app to start
     */
    private static final int CONFIGURATION_CHANGE_TIMEOUT = 5000;


    @Before
    public void startMainActivityFromHomeScreen() {

        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        // Launch the app
        context = InstrumentationRegistry.getContext();
        final Intent intent = context
                .getPackageManager()
                .getLaunchIntentForPackage(PACKAGE);

        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);

        //
        // Set up internals which allow us to peek to running app using Espresso
        //

        // get Apps db
        db = AppDatabase.getDatabase(context);

        // get dao for articles
        articleDao = db.articleModel();
    }


    @Test
    public void checkSettings() throws UiObjectNotFoundException, RemoteException {

        // find settings button based on buttons description (see activity_feed.xml)
        UiObject settingsButton = mDevice.findObject(new UiSelector().description("Settings"));

        // Simulate a click to bring up SettingsActivity with Preferences Fragement.
        settingsButton.clickAndWaitForNewWindow();

        // TODO: test seems to run also when configuration change is forced with rotation
        // TODO: but emulator doesn't build screen anymore, which is strange

        // rotate
        //mDevice.setOrientationLeft();

        // wait that activity builds itself again
        // mDevice.waitForIdle(CONFIGURATION_CHANGE_TIMEOUT);

        // go back
        pressBack();

        //
        // Articles list is filled properly
        //

        // GRAY BOX: find recycler view from running app, get adapter, find out amount of articles

        // check that list contains multiple items
        RecyclerView articleView  = (RecyclerView) mActivityRule.getActivity().findViewById(R.id.articleView);
        int itemCount = articleView.getAdapter().getItemCount();

        // check that view contains items
        Assert.assertTrue(itemCount > 0);

        // GRAY BOX: find out amount of articles from db of running app, compare to amount of articles in view of runninh app

        // get article count
        long dbCount = articleDao.countArticles();

        // check that db has same amount of items as adapter
        Assert.assertEquals(dbCount, itemCount);

        Log.d(TAG, "Found items :"+dbCount);

    }


}
