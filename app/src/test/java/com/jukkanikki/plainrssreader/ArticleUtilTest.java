package com.jukkanikki.plainrssreader;

import com.jukkanikki.plainrssreader.model.FeedWrapper;
import com.jukkanikki.plainrssreader.util.ArticlesUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jnikki on 11/8/17.
 */


public class ArticleUtilTest {

    String json = "{\"status\":\"ok\",\"feed\":{\"url\":\"http://rss.nytimes.com/services/xml/rss/nyt/Science.xml\",\"title\":\"NYT &gt; Science\",\"link\":\"https://www.nytimes.com/section/science?partner=rss&amp;emc=rss\",\"author\":\"\",\"description\":\"\",\"image\":\"https://static01.nyt.com/images/misc/NYT_logo_rss_250x40.png\"},\"items\":[{\"title\":\"ScienceTake: Wolves Know How to Work Together\",\"pubDate\":\"2017-11-07 08:30:31\",\"link\":\"https://www.nytimes.com/2017/11/07/science/wolves-dogs-cooperation.html?partner=rss&amp;emc=rss\",\"guid\":\"https://www.nytimes.com/2017/11/07/science/wolves-dogs-cooperation.html\",\"author\":\"JAMES GORMAN\",\"thumbnail\":\"\",\"description\":\"Wolves far outperformed dogs on a widely used test of cooperation in animals.\",\"content\":\"Wolves far outperformed dogs on a widely used test of cooperation in animals.\",\"enclosure\":[],\"categories\":[\"Dogs\",\"Wolves\",\"Proceedings of the National Academy of Sciences\"]}]}";


    String title1 = "ScienceTake: Wolves Know How to Work Together";

    @Test
    public void parseJsonTest () {
        // marshal to feed
        FeedWrapper feed = ArticlesUtil.convertToObjects(json);

        // check structure
        Assert.assertNotNull(feed);
        Assert.assertNotNull(feed.getItems());
        Assert.assertNotNull(feed.getItems().get(0));
        Assert.assertNotNull(feed.getItems().get(0).getTitle());

        // check values
        Assert.assertEquals(feed.getItems().get(0).getTitle(), title1);
    }
    
}
