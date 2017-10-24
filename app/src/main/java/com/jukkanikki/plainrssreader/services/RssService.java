package com.jukkanikki.plainrssreader.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.jukkanikki.plainrssreader.events.Events;
import com.jukkanikki.plainrssreader.http.HttpReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

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

        // writes content to file
        File file = writeContentToFile(url, content);

        // sends broadcast with files uri
        sendBroadcastWithFileUri(file);
    }

    private void contentReadyUsingDB (String url, String content) {

        // TODO: write content to SQLite
    }


    /**
     * Writes content to temp file
     *
     * file is written to temp files of android, it's not exposed to outsiders
     *
     * @param url url of content
     * @param content payload
     * @return file handle
     */
    private File writeContentToFile(String url, String content) {
        File file = null;

        // TODO: use try-with-resources
        try {
            String fileName = Uri.parse(url).getLastPathSegment();

            // create temp file used to keep file hidden from other apps
            file = File.createTempFile(fileName, null, getBaseContext().getCacheDir());

            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(content);

            myOutWriter.close();

            fOut.flush();
            fOut.close();

        } catch (IOException e) {
            // Error while creating file
            Log.e(TAG,"error saving result :"+e.getMessage());
            e.printStackTrace();
        }

        return file;
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
            localIntent.putExtra(Events.CONTENT_URL, file.toURI().toString()); // pointer to file

            // Broadcasts the Intent to receivers in this app.
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

            Log.d(TAG, "local intent sent with uri :"+file.toURI().toString());
        } else {
            Log.e(TAG, "File is null, can't send broadcast with URI");
        }
    }

}
