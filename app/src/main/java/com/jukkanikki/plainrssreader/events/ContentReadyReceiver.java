package com.jukkanikki.plainrssreader.events;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jukkanikki.plainrssreader.util.ArticlesUtil;
import com.jukkanikki.plainrssreader.model.FeedWrapper;
import com.jukkanikki.plainrssreader.util.FileUtil;

/**
 * React when feeds needs to be rendered
 *
 * receiver should force refresh of content:
 *    identifying feeds location
 *    reading data from persistent store,
 *    filling / creating recycler view adapter and
 *    notifying view recycler view that content should be repainted
 */
public class ContentReadyReceiver extends BroadcastReceiver {

    private static final String TAG = "ContentReadyReceiver";

    private RecyclerView articleView;

    /**
     * Inject article view
     * @param articleView article view
     */
    public void setArticleView(RecyclerView articleView) {
        this.articleView = articleView;
    }

    /**
     * This method is called when the BroadcastReceiver is receiving
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        // ready to update article view from  ...
        String uri = intent.getStringExtra(Events.CONTENT_URL); // get uri from extras
        Log.d(TAG,"Content ready at :"+uri.toString());

        String data = FileUtil.readFile(uri); //Get the text file
        Log.d(TAG,"read data from temp :"+data.substring(0,100));

        // Fill list of articles
        FeedWrapper feed = ArticlesUtil.convertToObjects(data);

        // bind feed to acticle view
        ArticlesUtil.bindView(context ,articleView, feed);
    }

}
