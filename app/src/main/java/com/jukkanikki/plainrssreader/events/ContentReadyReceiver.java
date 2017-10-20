package com.jukkanikki.plainrssreader.events;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

// TODO: Implement receiver
// receiver should force refresh of content:
//   reading data from persistent store,
//   filling / creating recycler view adapter and
//   notifying view recycler view that content should be repainted

public class ContentReadyReceiver extends BroadcastReceiver {

    private static final String TAG = "ContentReadyReceiver";

    /**
     * This method is called when the BroadcastReceiver is receiving
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"Content ready");
    }
}
