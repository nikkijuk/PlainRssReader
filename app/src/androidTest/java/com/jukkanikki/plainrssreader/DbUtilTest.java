package com.jukkanikki.plainrssreader;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.jukkanikki.plainrssreader.db.AppDatabase;
import com.jukkanikki.plainrssreader.db.Article;
import com.jukkanikki.plainrssreader.model.FeedWrapper;
import com.jukkanikki.plainrssreader.util.DbUtil;
import com.jukkanikki.plainrssreader.util.JsonUtil;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Simple roundtrip test which converts json to pojos, writes pojos to db and reads pojos from db
 */
public class DbUtilTest {

    // json from real NYT feed
    private static final String JSON = "{\"status\":\"ok\",\"feed\":{\"url\":\"http://rss.nytimes.com/services/xml/rss/nyt/Science.xml\",\"title\":\"NYT &gt; Science\",\"link\":\"https://www.nytimes.com/section/science?partner=rss&amp;emc=rss\",\"author\":\"\",\"description\":\"\",\"image\":\"https://static01.nyt.com/images/misc/NYT_logo_rss_250x40.png\"},\"items\":[{\"title\":\"ScienceTake: Wolves Know How to Work Together\",\"pubDate\":\"2017-11-07 08:30:31\",\"link\":\"https://www.nytimes.com/2017/11/07/science/wolves-dogs-cooperation.html?partner=rss&amp;emc=rss\",\"guid\":\"https://www.nytimes.com/2017/11/07/science/wolves-dogs-cooperation.html\",\"author\":\"JAMES GORMAN\",\"thumbnail\":\"\",\"description\":\"Wolves far outperformed dogs on a widely used test of cooperation in animals.\",\"content\":\"Wolves far outperformed dogs on a widely used test of cooperation in animals.\",\"enclosure\":[],\"categories\":[\"Dogs\",\"Wolves\",\"Proceedings of the National Academy of Sciences\"]}]}";

    // value from rss feed item
    private static final String TITLE_1 = "ScienceTake: Wolves Know How to Work Together";

    private Context context;

    AppDatabase db;

    @Before
    public void setUp() {
        // get context from android
        context = InstrumentationRegistry.getTargetContext();

        db = AppDatabase.getDatabase(context); // get db
    }

    /**
     * Write JSON to db - this test is run on android
     */
    @Test
    public void SaveJsonToDbAndReadTest () {

        // marshal to feed wrapper container
        FeedWrapper feed = JsonUtil.convertToObjects(JSON);

        // write articles from feed to SQLite db using Room
        DbUtil.populateDbFromFeed(db, feed);

        // get all articles
        List<Article> articles = DbUtil.readArticles(db);

        // check structure
        Assert.assertNotNull(articles);
        Assert.assertEquals(articles.size(), 1);

        // check values
        Article article = articles.get(0);
        Assert.assertEquals(article.getTitle(), TITLE_1);
    }
}
