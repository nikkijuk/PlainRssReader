package com.jukkanikki.plainrssreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.jukkanikki.plainrssreader.adapters.FeedAdapter;
import com.jukkanikki.plainrssreader.http.HttpReader;
import com.jukkanikki.plainrssreader.model.FeedWrapper;

public class FeedActivity extends AppCompatActivity {

    // Key for preferences reading
    private static final String KEY_PREF_SOURCE = "rss_source";

    // Xml to Json conversion api
    private final static String RSS_TO_JSON_API_API = "https://api.rss2json.com/v1/api.json?rss_url=";
    
    // Default feed used
    private final static String DEFAULT_RSS_URL ="http://rss.nytimes.com/services/xml/rss/nyt/Science.xml";

    private RecyclerView recyclerView;

    /**
    * Called when activity is created
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("News"); // TODO: move hardcoded value to Strings.xml
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
    * Called when activity comes visible
    */
    @Override
    protected void onResume () {
        super.onResume();
        loadRSS();
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
     * Load rss feed content asynchronously
     */
    private void loadRSS() {
        AsyncTask<String, String, String> loadRSSAsync = new AsyncTask<String, String, String>() {

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected String doInBackground(String... params) {
                return HttpReader.getData(params[0]);
            }

            @Override
            protected void onPostExecute(String s) {
                FeedWrapper feed = new Gson().fromJson(s, FeedWrapper.class);
                FeedAdapter adapter = new FeedAdapter(feed, getBaseContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };

        String rssUrl = getRssUrl();
        loadRSSAsync.execute(String.format("%s%s", RSS_TO_JSON_API_API ,rssUrl));
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
     * Opens settings activity
     * @param view button which onClick handler this method is
     */
    public void openSettings(View view) {
        Intent settingsIntent = new Intent(this,SettingsActivity.class);
        startActivity(settingsIntent);
    }
}
