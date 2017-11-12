package com.jukkanikki.plainrssreader.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.jukkanikki.plainrssreader.db.AppDatabase;
import com.jukkanikki.plainrssreader.Events;
import com.jukkanikki.plainrssreader.http.HttpReader;
import com.jukkanikki.plainrssreader.model.FeedWrapper;
import com.jukkanikki.plainrssreader.util.DbUtil;
import com.jukkanikki.plainrssreader.util.FileUtil;
import com.jukkanikki.plainrssreader.util.JsonUtil;

import java.io.File;

/**
 * Async logic and persistence of articles
 *
 * As intent services might be hard to test as they are async and initialized by system
 * there's tests for all of operations which this service does.
 *
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 * rss service
 *   receives url as parameter
 *   retrieves feed using http,
 *   converts feed to pojos using gson,
 *   saves pojos to persistent storage (with room of file) and
 *   notifies views using local broadcasts that feed is updated
 *
 * Using separate service and broadcast receiver makes process completely async
 *
 * More: https://developer.android.com/training/best-background.html
 */
public class RssService extends IntentService {

    private static final String TAG = "RssService";

    public RssService() {
        super("RssService");
    }

    /**
     * Handles request to load rss feed from external source.
     * Source Url is saved in intent, which is given as parameter.
     *
     * Most of logic used in this class is implemented using utility classes and static methods
     * which are easy to test separately in unit and instrumented integration tests.
     *
     * @param intent request for loading rss feed and storing it to database
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        // base context might not be present during construction of intent service
        Context context = getBaseContext(); // get ctx

        // get db
        AppDatabase db = AppDatabase.getDatabase(context);

        // gets url from the incoming Intent
        String urlString = intent.getDataString();
        Log.d(TAG,"Background processing started for url:"+urlString);

        // get data using http
        String data = HttpReader.getData(urlString);
        Log.d(TAG,"received data :"+data.substring(0,data.length() > 2000?2000:data.length()));

        // save content to file identified with url
        // writing file might not be really needed except for debugging
        File file = FileUtil.createTempFile(context, urlString, data);

        // marshal to feed
        FeedWrapper feed = JsonUtil.convertToObjects(data);

        // write articles from feed to SQLite db using Room
        DbUtil.populateDbFromFeed(db, feed);

        // sends broadcast with files uri
        sendBroadcastWithFileUri(file);

        Log.d(TAG,"Background processing finished for url:"+urlString);
    }

    /**
     * Send notification that new article file is ready to be consumer
     *
     * @param file file to be consumed
     */
    private void sendBroadcastWithFileUri(File file) {
        if (file != null) {
            Intent localIntent = new Intent(Events.CONTENT_READY_ACTION);  // intent to send locally

            // file uri is stored in instance only for debugging purposes
            String fileUri = file.toURI().toString();
            localIntent.putExtra(Events.CONTENT_READY_FILE_URI, fileUri); // pointer to file

            // Broadcasts the Intent to receivers in this app.
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

            Log.d(TAG, "local intent sent with uri :"+ fileUri);
        } else {
            Log.e(TAG, "File is null, can't send broadcast with URI");
        }
    }

}
