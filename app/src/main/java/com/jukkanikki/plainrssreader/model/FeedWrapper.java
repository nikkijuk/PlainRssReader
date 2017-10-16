package com.jukkanikki.plainrssreader.model;

import java.util.List;

public class FeedWrapper {

    public String status;
    public FeedHeaders feed;
    public List<FeedItem> items;

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
