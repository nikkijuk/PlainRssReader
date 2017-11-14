package com.jukkanikki.plainrssreader;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.jukkanikki.plainrssreader.db.AppDatabase;
import com.jukkanikki.plainrssreader.db.Article;
import com.jukkanikki.plainrssreader.db.ArticleDao;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;

/**
 * Tests arcticleDao and appDatabase directly
 *
 * Test sets up in memory SQLLite db instance and uses it
 */
@RunWith(AndroidJUnit4.class)
public class ArticleDbTests {

    private Context context;

    private AppDatabase db;

    private ArticleDao articleDao;

    /**
     * setup db and dao
     */
    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        db = AppDatabase.getDatabase(context); // get db
        articleDao = db.articleModel(); // get dao for articles
    }

    @After
    public void tearDown() {
        db.destroyInstance(); // free db instance from memory
    }

    /**
     * Create, write and read article
     */
    @Test
    public void storeAndReadArticles() {

        // this wasn't needed with in memory db - now db can contain records, so prepare it
        articleDao.deleteAll(); // see that it's empty

        Article article = new Article();
        article.guid = "guid-1";
        article.title = "title";
        article.pubDate = "1.1.2020";
        article.link = "linkLocation";
        article.content = "articleContent";

        articleDao.insertArticle(article); // insert article

        long count = articleDao.countArticles(); // get amount of articles
        Assert.assertEquals(1, count);

        List<Article> articles = articleDao.allArticles(); // get list of articles

        Assert.assertEquals(1, articles.size());

        Article article1 = articles.get(0);
        Assert.assertNotNull(article1);

        String content1 = article1.getContent();
        Assert.assertNotNull(content1);

        Assert.assertEquals("articleContent", content1);
    }
}