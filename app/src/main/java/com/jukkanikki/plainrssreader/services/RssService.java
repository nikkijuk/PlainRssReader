package com.jukkanikki.plainrssreader.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.jukkanikki.plainrssreader.events.Events;
import com.jukkanikki.plainrssreader.http.HttpReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;

// TODO: Async logic and persistence of articles
// impement service which
//   receives url as parameter
//   retrieves feed using http,
//   converts feed to pojos using gson,
//   saves pojos to persistent storage with room and
//   notifies views using local broadcasts that feed is updated
//
// More: https://developer.android.com/training/best-background.html

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
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

        String data = HttpReader.getData(urlString);
        Log.d(TAG,"received data :"+data.substring(1,100));

        contentReady(urlString, data);

        Log.d(TAG,"Background processing finished for url:"+urlString);
    }

    private void contentReady(String url, String content) {

        File file = writeContentToFile(url, content);

        sendBroadcastWithFileUri(file);

    }

    /**
     * Writes content to temp file
     *
     * @param url url of content
     * @param content payload
     * @return file handle
     */
    private File writeContentToFile(String url, String content) {
        File file = null;

        try {
            String fileName = Uri.parse(url).getLastPathSegment();
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
            localIntent.putExtra("URL", file.toURI().toString()); // pointer to file

            // Broadcasts the Intent to receivers in this app.
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

            Log.d(TAG, "local intent sent");
        } else {
            Log.e(TAG, "File is null, can't send broadcast with URI");
        }
    }

}
