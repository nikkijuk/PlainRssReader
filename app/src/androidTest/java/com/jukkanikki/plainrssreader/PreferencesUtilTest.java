package com.jukkanikki.plainrssreader;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.jukkanikki.plainrssreader.util.PreferencesUtil;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class PreferencesUtilTest {

    private static final String DEFAULT_URL = "https://api.rss2json.com/v1/api.json?rss_url=http://rss.nytimes.com/services/xml/rss/nyt/Science.xml";

    private static final String SAVED_URL = "https://api.rss2json.com/v1/api.json?rss_url=X";

    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
    }

    /**
     * test get default url
     */
    @Test
    public void defaultUrlTest () {
        //set prefs to null
        PreferencesUtil.setRssUrl(context, null);

        // get url
        String rssUrl = PreferencesUtil.getRssUrl(context);

        // check that default value is returned
        assertEquals (DEFAULT_URL, rssUrl);
    }

    /**
     * test get saved url
     */
    @Test
    public void savdUrlTest () {
        //set prefs to null
        PreferencesUtil.setRssUrl(context, "X");

        // get url
        String rssUrl = PreferencesUtil.getRssUrl(context);

        // check that set value is returned
        assertEquals (SAVED_URL, rssUrl);
    }


}
