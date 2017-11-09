package com.jukkanikki.plainrssreader.model;

import java.util.List;

/**
* Feed wrapper represents root element of json structure
*
* Many fields are never used in this application, but they are here since they MIGHT be needed.
* YAGNI not followed, sorry. Here the main motivation is to show how whole document can be converted.
*/
public class FeedWrapper {

    public String status;
    public FeedHeaders feed; // header info, metadata of feed, not used
    public List<FeedItem> items; // actual articles

    public FeedWrapper(String status, FeedHeaders feed, List<FeedItem> items) {
        this.status = status;
        this.feed = feed;
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FeedHeaders getFeed() {
        return feed;
    }

    public void setFeed(FeedHeaders feed) {
        this.feed = feed;
    }

    public List<FeedItem> getItems() {
        return items;
    }

    public void setItems(List<FeedItem> items) {
        this.items = items;
    }

}
