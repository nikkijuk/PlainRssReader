package com.jukkanikki.plainrssreader.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * This content provider is ment to share read only data with other apps
 */
public class ArticleContentProvider extends ContentProvider {

    public static final String READ_ONLY_NOT_SUPPORTED = "Read only - not supported";

    public ArticleContentProvider() {
    }

    @Override
    public String getType(Uri uri) {
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Which is better? return null or throw exception?
        // throw new UnsupportedOperationException(READ_ONLY_NOT_SUPPORTED);
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // Which is better? return 0 or throw exception?
        // throw new UnsupportedOperationException(READ_ONLY_NOT_SUPPORTED);
        return 0;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Which is better? return 0 or throw exception?
        //throw new UnsupportedOperationException(READ_ONLY_NOT_SUPPORTED);
        return 0;
    }

}
