package com.jukkanikki.plainrssreader.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jukkanikki.plainrssreader.db.AppDatabase;
import com.jukkanikki.plainrssreader.db.Article;
import com.jukkanikki.plainrssreader.util.ArticlesUtil;
import com.jukkanikki.plainrssreader.util.DbUtil;

import java.util.List;

/**
 * React when articles needs to be rendered
 *
 * receiver should force refresh of content:
 *    reading data from persistent store,
 *    filling / creating recycler view adapter and
 *    notifying view recycler view that content should be repainted
 */
public class ContentDbReadyReceiver extends BroadcastReceiver {

    private static final String TAG = "ContentDbReadyReceiver";

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

        // get db
        AppDatabase db = AppDatabase.getInMemoryDatabase(context);

        // get all articles
        List<Article> articles = DbUtil.readArticles(db);

        // bind articles to acticle view
        ArticlesUtil.bindViewToArticles(context ,articleView, articles);

        Log.d(TAG,"read articles from db");
    }
}
