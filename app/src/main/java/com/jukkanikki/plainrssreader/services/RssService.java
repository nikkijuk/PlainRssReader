package com.jukkanikki.plainrssreader.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.jukkanikki.plainrssreader.events.Events;
import com.jukkanikki.plainrssreader.http.HttpReader;
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
        String urlString = intent.getDataString();  // Gets url from the incoming Intent
        Log.d(TAG,"Background processing started for url:"+urlString);

        String data = HttpReader.getData(urlString); // get data
        Log.d(TAG,"received data :"+data.substring(0,100));

        // send result to broadcast receiver
        contentReadyUsingFile(urlString, data);

        Log.d(TAG,"Background processing finished for url:"+urlString);
    }

    /**
     * save content to file identified with url and send url to broadcast receiver
     *
     * @param url
     * @param content
     */
    private void contentReadyUsingFile (String url, String content) {

        File file = FileUtil.createTempFile(getBaseContext(), url, content);

        // sends broadcast with files uri
        sendBroadcastWithFileUri(file);
    }

    private void contentReadyUsingDB (String url, String content) {
        // TODO: write content to SQLite
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
