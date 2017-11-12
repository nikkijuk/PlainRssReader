package com.jukkanikki.plainrssreader;

import com.jukkanikki.plainrssreader.model.FeedWrapper;
import com.jukkanikki.plainrssreader.util.JsonUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * Json related operations test
 *
 * Test unit of work - does not have dependencies to android
 */
public class JsonUtilTest {

    private static final String EMPTY_JSON = "{}";

    private static final String JSON = "{\"status\":\"ok\",\"feed\":{\"url\":\"http://rss.nytimes.com/services/xml/rss/nyt/Science.xml\",\"title\":\"NYT &gt; Science\",\"link\":\"https://www.nytimes.com/section/science?partner=rss&amp;emc=rss\",\"author\":\"\",\"description\":\"\",\"image\":\"https://static01.nyt.com/images/misc/NYT_logo_rss_250x40.png\"},\"items\":[{\"title\":\"ScienceTake: Wolves Know How to Work Together\",\"pubDate\":\"2017-11-07 08:30:31\",\"link\":\"https://www.nytimes.com/2017/11/07/science/wolves-dogs-cooperation.html?partner=rss&amp;emc=rss\",\"guid\":\"https://www.nytimes.com/2017/11/07/science/wolves-dogs-cooperation.html\",\"author\":\"JAMES GORMAN\",\"thumbnail\":\"\",\"description\":\"Wolves far outperformed dogs on a widely used test of cooperation in animals.\",\"content\":\"Wolves far outperformed dogs on a widely used test of cooperation in animals.\",\"enclosure\":[],\"categories\":[\"Dogs\",\"Wolves\",\"Proceedings of the National Academy of Sciences\"]}]}";

    private static final String TITLE_1 = "ScienceTake: Wolves Know How to Work Together";

    /**
     * Mashall JSON to pojos
     */
    @Test
    public void parseJsonTest () {

        // marshal to feed
        FeedWrapper feed = JsonUtil.convertToObjects(JSON);

        // check structure
        Assert.assertNotNull(feed);
        Assert.assertNotNull(feed.getItems());
        Assert.assertNotNull(feed.getItems().get(0));
        Assert.assertNotNull(feed.getItems().get(0).getTitle());

        // check values
        Assert.assertEquals(feed.getItems().get(0).getTitle(), TITLE_1);
    }

    /**
     * Mashall JSON to pojos
     */
    @Test
    public void parseEmptyJsonTest () {

        // marshal to feed
        FeedWrapper feed = JsonUtil.convertToObjects(EMPTY_JSON);

        // check structure
        Assert.assertNotNull(feed);
        Assert.assertNull(feed.getItems()); // no items parsed
    }

}
