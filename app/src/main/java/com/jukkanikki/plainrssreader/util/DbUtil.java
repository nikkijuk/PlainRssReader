package com.jukkanikki.plainrssreader.util;


import android.util.Log;

import com.jukkanikki.plainrssreader.db.AppDatabase;
import com.jukkanikki.plainrssreader.db.Article;
import com.jukkanikki.plainrssreader.model.FeedItem;
import com.jukkanikki.plainrssreader.model.FeedWrapper;

public class DbUtil {

    private static final String TAG = "DbUtil";

    private DbUtil () {}

    /**
     * Private helper method to create and add single article to db
     *
     * @param db
     * @param guid
     * @param title
     * @param pubDate
     * @param link
     * @return
     */
    private static Article addArticle (final AppDatabase db, final String guid, final String title,
                                       final String pubDate, final String link,final String content) {
        Article article = new Article();
        article.guid = guid;
        article.title = title;
        article.pubDate = pubDate;
        article.link = link;
        article.content = content;
        db.articleModel().insertArticle(article);
        return article;
    }

    /**
     * Convert feed to articles and save to db
     *
     * @param db
     * @param feed
     */
    public static void populateDbFromFeed(AppDatabase db, FeedWrapper feed) {
        db.articleModel().deleteAll(); // empty

        // add all articles to db
        for (FeedItem item:feed.getItems()) {
            addArticle(db, item.getGuid(), item.getTitle(), item.getPubDate(), item.getLink(), item.getContent());
        }

        Log.d(TAG,"written articles to db");

    }

}