package com.jukkanikki.plainrssreader.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.jukkanikki.plainrssreader.events.Events;

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

        contentReady();

        Log.d(TAG,"Background processing finished for url:"+urlString);

    }

    private void contentReady() {
        Intent localIntent = new Intent(Events.CONTENT_READY_ACTION); // intent to send locally

        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

        Log.d(TAG,"local intent sent");
    }

}
