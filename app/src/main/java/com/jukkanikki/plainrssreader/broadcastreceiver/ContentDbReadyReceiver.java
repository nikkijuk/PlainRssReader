package com.jukkanikki.plainrssreader.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jukkanikki.plainrssreader.adapters.ArticleAdapter;
import com.jukkanikki.plainrssreader.db.AppDatabase;
import com.jukkanikki.plainrssreader.db.Article;
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

    /**
     * reference to view
     */
    private RecyclerView articleView;

    public void BroadcastReceiver () {
    }

    /**
     * Inject article view
     *
     * Having reference from broadcast receiver to view might be really dangerous
     * if view has shorter lifecycle than broadcast receiver.
     *
     * @param articleView article view
     */
    public void setArticleView(RecyclerView articleView) {
        // set reference to view
        this.articleView = articleView;
    }

    /**
     * onReceive is called when the  ContentDbReadyReceiver is receiving events from RssService.
     *
     * Broadcast receiver is registered at FeedActivity during onResume and unregistered during onPause,
     * which means that this method is called only when FeedActiviy is active and in foreground
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        // db comes from AppDatabase singleton
        // as db is already initialized this operation is not expensive

        AppDatabase db = AppDatabase.getDatabase(context); // get db
        List<Article> articles = DbUtil.readArticles(db); // get all articles

        // bind articles to acticle view
        // creating new adapter after each update of values might not be right thing to do ..
        // it would need to be studied if it is enough to have single instance of adapter and refilling it

        ArticleAdapter adapter = new ArticleAdapter(articles, context); // create adapter
        articleView.setAdapter(adapter); // set adapter
        adapter.notifyDataSetChanged(); // inform adapter that it should update

        Log.d(TAG,"read articles from db");
    }

}
