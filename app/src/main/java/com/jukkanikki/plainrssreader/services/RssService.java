package com.jukkanikki.plainrssreader.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

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

        Log.d(TAG,"url is :"+urlString);

    }

}
