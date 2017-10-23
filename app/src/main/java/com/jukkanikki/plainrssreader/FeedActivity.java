package com.jukkanikki.plainrssreader;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.jukkanikki.plainrssreader.adapters.FeedAdapter;
import com.jukkanikki.plainrssreader.events.ContentReadyReceiver;
import com.jukkanikki.plainrssreader.events.Events;
import com.jukkanikki.plainrssreader.http.HttpReader;
import com.jukkanikki.plainrssreader.model.FeedWrapper;
import com.jukkanikki.plainrssreader.services.RssService;

public class FeedActivity extends AppCompatActivity {

    private static final String TAG = "FeedActivity";

    // Key for preferences reading
    private static final String KEY_PREF_SOURCE = "rss_source";

    // Xml to Json conversion api
    private final static String RSS_TO_JSON_API_API = "https://api.rss2json.com/v1/api.json?rss_url=";
    
    // Default feed used
    private final static String DEFAULT_RSS_URL ="http://rss.nytimes.com/services/xml/rss/nyt/Science.xml";

    // view to show feeds
    private RecyclerView articleView;

    // Instantiates a new receiver
    ContentReadyReceiver contentReadyReceiver = new ContentReadyReceiver();

    // The filter's action is BROADCAST_ACTION
    IntentFilter contentReadyIntentFilter = new IntentFilter(Events.CONTENT_READY_ACTION);

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

    }

    /**
    * Called when activity comes visible
    */
    @Override
    protected void onResume () {
        super.onResume();
        registerContentReadyReceiver(); // listen content ready events

        // when feed is loaded at onResume
        // list is refreshed always when activity comes visible
        loadRSS();
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
        startActivity(settingsIntent);
    }

    /**
     * Load rss feed content asynchronously
     */
    private void loadRSS() {
        AsyncTask<String, String, String> loadRSSAsync = new AsyncTask<String, String, String>() {

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected String doInBackground(String... params) {
                return HttpReader.getData(params[0]); // get http string
            }

            @Override
            protected void onPostExecute(String s) {
                FeedWrapper feed = new Gson().fromJson(s, FeedWrapper.class); // create object model
                FeedAdapter adapter = new FeedAdapter(feed, getBaseContext()); // create adapter
                articleView.setAdapter(adapter); // set adapter
                adapter.notifyDataSetChanged(); // inform adapter that it should updatee
            }
        };

        String rssUrl = getRssUrl(); // get url from preferences or default
        loadRSSAsync.execute(String.format("%s%s", RSS_TO_JSON_API_API ,rssUrl)); // start execution of async task

        // Todo: call backgroud service to read feed
        // Note: this method is left uncommented, so that it is possibly to
        // follow flow of operations
        callRssService(rssUrl);
    }

    /**
     * Reads url, and uses default if preferences is empty
     */
     @NonNull
    private String getRssUrl() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPref.getString(KEY_PREF_SOURCE, DEFAULT_RSS_URL);
    }

    /**
     * Register local receiver for content ready broadcasts
     */
    private void registerContentReadyReceiver() {
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
    private void callRssService(String url) {
        Intent intent = new Intent(this, RssService.class);
        intent.setData(Uri.parse(url)); // set url to data
        startService(intent); // Starts the IntentService
        Log.d(TAG,"Called rss service");
    }

}
