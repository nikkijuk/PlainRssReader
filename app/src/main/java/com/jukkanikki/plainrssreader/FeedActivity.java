package com.jukkanikki.plainrssreader;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.jukkanikki.plainrssreader.broadcastreceiver.ContentDbReadyReceiver;
import com.jukkanikki.plainrssreader.services.RssService;
import com.jukkanikki.plainrssreader.util.PreferencesUtil;

/**
 * Main activity of app
 */
public class FeedActivity extends AppCompatActivity {

    private static final String TAG = "FeedActivity";

    // view to show feeds
    private RecyclerView articleView;

    // receiver for content ready notifications
    // --> content from db
    ContentDbReadyReceiver contentReadyReceiver = new ContentDbReadyReceiver();

    /**
    * Called when activity is created
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // get toolbar and set it in use
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("News"); // TODO: move hardcoded value to Strings.xml
        setSupportActionBar(toolbar);

        // find view and set layout
        articleView = findViewById(R.id.articleView);
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
        articleView.setLayoutManager(linearLayoutManager);

        // define which view is updated when content is ready
        contentReadyReceiver.setArticleView(articleView);
    }

    /**
    * Called when activity comes visible
    * As feed is loaded at onResume list of articles is refreshed always when activity comes visible
    */
    @Override
    protected void onResume () {
        super.onResume();
        registerContentReadyReceiver(); // listen content ready events

        // call backgroud service to read feed
        // using intent service + broadcast receiver
        loadRssUsingIntentService();
    }

    /**
     * Called when activity stops
     */
    @Override
    protected void onPause () {
        super.onPause();
        unRegisterContentReadyReceiver(); // stop listening content ready events
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Opens settings activity
     * @param view button which onClick handler this method is
     */
    public void openSettings(View view) {
        Intent settingsIntent = new Intent(this,SettingsActivity.class);
        startActivity(settingsIntent); // opens settings activity
    }


    /**
     * Register local receiver for content ready broadcasts
     */
    private void registerContentReadyReceiver() {

        // The filter's action is BROADCAST_ACTION
        IntentFilter contentReadyIntentFilter = new IntentFilter(Events.CONTENT_READY_ACTION);

        // Registers the receiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                contentReadyReceiver,
                contentReadyIntentFilter);

        Log.d(TAG,"Registered content ready receiver");
    }

    /**
     * UnRegister local receiver for content ready broadcasts
     */
    private void unRegisterContentReadyReceiver() {
        // UnRegisters the receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(contentReadyReceiver);

        Log.d(TAG,"Unregistered content ready receiver");
    }


    /**
     * Creates a new Intent to start the RssService. Passes a URI in the Intent's "data" field.
     */
    private void loadRssUsingIntentService() {
        String rssUrl = PreferencesUtil.getRssUrl(this); // get url from preferences or default
        Intent intent = new Intent(this, RssService.class); // define which service is to be started
        intent.setData(Uri.parse(rssUrl)); // set url to data

        // Starts the IntentService
        // intent service is started service and doesn't have any kind of reference to activity calling it
        startService(intent);

        Log.d(TAG,"Called rss service");
    }

}
