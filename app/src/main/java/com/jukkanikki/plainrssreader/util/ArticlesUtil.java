package com.jukkanikki.plainrssreader.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.jukkanikki.plainrssreader.adapters.ArticleAdapter;
import com.jukkanikki.plainrssreader.db.Article;
import com.jukkanikki.plainrssreader.model.FeedWrapper;

import java.util.List;

/**
 * Helper methods to work with articles
 */
public class ArticlesUtil {

    private ArticlesUtil () {} // helper class, not possible to instantiate

    /**
     * Binds articles to view by creating needed adapter, setting it and requesting state refresh
     *
     * @param context
     * @param view
     * @param articles
     */
    public static void bindViewToArticles(Context context, RecyclerView view, List<Article> articles) {
        // TODO: creating new adapter after each update of values might not be right thing to do ..
        ArticleAdapter adapter = new ArticleAdapter(articles, context); // create adapter
        view.setAdapter(adapter); // set adapter
        adapter.notifyDataSetChanged(); // inform adapter that it should update
    }

}
