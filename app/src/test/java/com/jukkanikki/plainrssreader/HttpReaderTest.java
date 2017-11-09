package com.jukkanikki.plainrssreader;

import com.jukkanikki.plainrssreader.http.HttpReader;
import com.jukkanikki.plainrssreader.model.FeedWrapper;
import com.jukkanikki.plainrssreader.util.ArticlesUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test http related operations
 *
 * NOTE: tests using live data - this test is unreliable, as live endpoints might not be available when test is run
 *
 * NOTE: this test could use mocking to prevent usage of live data
 */
public class HttpReaderTest {

    private static final String EMPTY_JSON = "{}";

    private static final String VALID_URL = "https://api.rss2json.com/v1/api.json?rss_url=http://rss.nytimes.com/services/xml/rss/nyt/Science.xml";

    private static final String INVALID_URL = "https://failsforsure.com";


    /**
     * Retrieve feed using http
     */
    @Test
    public void getContentFromValidUrl () {

        // read data from valid url
        String data = HttpReader.getData(VALID_URL);

        // check structure
        Assert.assertNotNull(data);

        // Note: content can't be checked - http is retrieved in live endpoint
        // endpoint would need to be mocked with static content
    }

    /**
     *  Retrieve feed using http -- url is invalid
     */
    @Test
    public void getContentFromInvalidUrl () {

        // read data - url invalid
        String data = HttpReader.getData(INVALID_URL);

        // check structure
        Assert.assertNotNull(data);

        // when request fails empty json is returned
        Assert.assertEquals(EMPTY_JSON, data);

    }

}
