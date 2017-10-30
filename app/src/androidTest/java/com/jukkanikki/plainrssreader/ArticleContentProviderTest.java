package com.jukkanikki.plainrssreader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.jukkanikki.plainrssreader.contentprovider.ArticleContentProvider;
import com.jukkanikki.plainrssreader.db.AppDatabase;
import com.jukkanikki.plainrssreader.db.Article;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ArticleContentProviderTest {

    private ContentResolver mContentResolver;

    AppDatabase db;

    @Before
    public void setUp() {
        final Context context = InstrumentationRegistry.getTargetContext();

        // initialize db
        db = AppDatabase.getInMemoryDatabase(context);

        mContentResolver = context.getContentResolver();
    }

    @After
    public void tearDown() {

        // free db instance from memory
        db.destroyInstance();
    }

    @Test
    public void article_initiallyEmpty() {

        // get articles
        final Cursor cursor = mContentResolver.query(ArticleContentProvider.URI_ARTICLE,
                new String[]{Article.COLUMN_TITLE}, null, null, null);

        // database should be empty
        Assert.assertThat(cursor, org.hamcrest.Matchers.notNullValue());
        Assert.assertThat(cursor.getCount(), org.hamcrest.Matchers.is(0));
        cursor.close();
    }

    @Test
    public void article_query() {

        // prepare test data
        Article article = new Article();
        article.guid = "1";
        article.title = "test";
        article.pubDate = "1.1.2020";
        article.link = "linkLocation";
        article.content = "articleContent";

        // add thru dao as content provider is read only
        db.articleModel().insertArticle(article); // insert article

        // read data thru content provider
        final Cursor cursor = mContentResolver.query(ArticleContentProvider.URI_ARTICLE,
                new String[]{Article.COLUMN_TITLE}, null, null, null);

        // check that inserted and read match
        Assert.assertThat(cursor, org.hamcrest.Matchers.notNullValue());
        Assert.assertThat(cursor.getCount(), org.hamcrest.Matchers.is(1));
        Assert.assertThat(cursor.moveToFirst(), org.hamcrest.Matchers.is(true));
        Assert.assertThat(cursor.getString(cursor.getColumnIndexOrThrow(Article.COLUMN_TITLE)), org.hamcrest.Matchers.is("test"));
        cursor.close();
    }
}
