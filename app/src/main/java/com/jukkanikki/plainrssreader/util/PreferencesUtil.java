package com.jukkanikki.plainrssreader.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

/**
 * Helper methods to work with preferences
 */
public class PreferencesUtil {

    private PreferencesUtil () {}

    // Key for preferences reading
    private static final String KEY_PREF_SOURCE = "rss_source";

    // Xml to Json conversion api
    private final static String RSS_TO_JSON_API_API = "https://api.rss2json.com/v1/api.json?rss_url=";

    // Default feed used
    private final static String DEFAULT_RSS_URL ="http://rss.nytimes.com/services/xml/rss/nyt/Science.xml";

    // Please see here possible nyt feeds for testing
    //
    // http://www.nytimes.com/services/xml/rss/index.html
    //
    // ex. http://rss.nytimes.com/services/xml/rss/nyt/Arts.xml

    /**
     * Reads url, and uses default if preferences is empty
     */
    @NonNull
    public static String getRssUrl(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String rssUrl = sharedPref.getString(KEY_PREF_SOURCE, DEFAULT_RSS_URL);
        return String.format("%s%s", RSS_TO_JSON_API_API ,rssUrl);
    }

    /**
     * sets url
     */
    public static void setRssUrl(Context context, String rssUrl) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.edit().putString(KEY_PREF_SOURCE, rssUrl).commit();
    }

}
