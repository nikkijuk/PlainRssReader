package com.jukkanikki.plainrssreader.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.jukkanikki.plainrssreader.db.AppDatabase;
import com.jukkanikki.plainrssreader.events.Events;
import com.jukkanikki.plainrssreader.http.HttpReader;
import com.jukkanikki.plainrssreader.model.FeedWrapper;
import com.jukkanikki.plainrssreader.util.ArticlesUtil;
import com.jukkanikki.plainrssreader.util.DbUtil;
import com.jukkanikki.plainrssreader.util.FileUtil;

import java.io.File;

/**
 * Async logic and persistence of articles
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
 * Using separate service and brodcast receiver makes process completely async
 *
 * More: https://developer.android.com/training/best-background.html
 */
public class RssService extends IntentService {

    private static final String TAG = "RssService";

    public RssService() {
        super("RssService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Context context = getBaseContext();

        // get db
        AppDatabase db = AppDatabase.getInMemoryDatabase(context);

        // gets url from the incoming Intent
        String urlString = intent.getDataString();
        Log.d(TAG,"Background processing started for url:"+urlString);

        // get data using http
        String data = HttpReader.getData(urlString);
        Log.d(TAG,"received data :"+data.substring(0,data.length() > 100?100:data.length()));

        // save content to file identified with url
        File file = FileUtil.createTempFile(context, urlString, data);

        // marshal to feed
        FeedWrapper feed = ArticlesUtil.convertToObjects(data);

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
            Intent localIntent;//localIntent = new Intent(Events.CONTENT_READY_ACTION, Uri.parse(file.toURI().toString())); // intent to send locally

            localIntent = new Intent(Events.CONTENT_READY_ACTION);  // intent to send locally

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
