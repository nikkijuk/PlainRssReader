package com.jukkanikki.plainrssreader;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.jukkanikki.plainrssreader.db.AppDatabase;
import com.jukkanikki.plainrssreader.db.ArticleDao;
import com.jukkanikki.plainrssreader.util.PreferencesUtil;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class PreferencesUtilTest {

    private static final String VALID_URL = "https://api.rss2json.com/v1/api.json?rss_url=http://rss.nytimes.com/services/xml/rss/nyt/Science.xml";

    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
    }

    /**
     * get Url
     */
    @Test
    public void getValidUrl () {
        //set prefs to null
        PreferencesUtil.setRssUrl(context, null);

        // get url
        String rssUrl = PreferencesUtil.getRssUrl(context);

        // check that default value is returned
        assertEquals (VALID_URL, rssUrl);
    }


    }
