package com.jukkanikki.plainrssreader.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jukkanikki.plainrssreader.db.AppDatabase;
import com.jukkanikki.plainrssreader.db.Article;

import java.util.List;

/**
 * This content provider is ment to share read only data with other apps
 *
 * See Room ORM documentation for implementation of data access methods
 *
 * Sample: https://github.com/googlesamples/android-architecture-components/tree/master/PersistenceContentProviderSample
 */
public class ArticleContentProvider extends ContentProvider {

    /** The authority of this content provider. */
    public static final String AUTHORITY = "com.nikkijuk.article.provider";

    /** The URI for the Article table. */
    public static final Uri URI_ARTICLE = Uri.parse(
            "content://" + AUTHORITY + "/" + Article.TABLE_NAME);

    /** The match code for some items in the Cheese table. */
    private static final int CODE_ARTICLE_DIR = 1;

    /** The match code for an item in the Cheese table. */
    private static final int CODE_ARTICLE_ITEM = 2;

    /** The URI matcher. */
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, Article.TABLE_NAME, CODE_ARTICLE_DIR);
        MATCHER.addURI(AUTHORITY, Article.TABLE_NAME + "/*", CODE_ARTICLE_ITEM);
    }

    public static final String READ_ONLY_NOT_SUPPORTED = "Read only - not supported";

    public ArticleContentProvider() {
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_ARTICLE_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + Article.TABLE_NAME;
            case CODE_ARTICLE_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + Article.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_ARTICLE_DIR || code == CODE_ARTICLE_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            // get db
            AppDatabase db = AppDatabase.getDatabase(context);

            final Cursor cursor;
            if (code == CODE_ARTICLE_DIR) {
                cursor = db.articleModel().cursorAllArticles();
            } else {
                cursor = db.articleModel().cursorExactArticle(uri.getLastPathSegment());
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    /**
     * Not supported
     *
     * @param uri
     * @param values
     * @return
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Which is better? return null or throw exception?
        // throw new UnsupportedOperationException(READ_ONLY_NOT_SUPPORTED);
        return null;
    }

    /**
     * Not supported
     *
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // Which is better? return 0 or throw exception?
        // throw new UnsupportedOperationException(READ_ONLY_NOT_SUPPORTED);
        return 0;

    }

    /**
     * Not supported
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Which is better? return 0 or throw exception?
        //throw new UnsupportedOperationException(READ_ONLY_NOT_SUPPORTED);
        return 0;
    }

}
