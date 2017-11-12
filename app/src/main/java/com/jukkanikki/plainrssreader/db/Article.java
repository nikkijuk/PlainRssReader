package com.jukkanikki.plainrssreader.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.support.annotation.NonNull;

/**
 * Article content.
 *
 * Added Room annotations for metadata.
 *
 * Id is generated automatically.
 *
 * Please see: https://developer.android.com/training/data-storage/room/defining-data.html
 */
@Entity(tableName = Article.TABLE_NAME)
public class Article {

    // The name of the table.
    public static final String TABLE_NAME = "article";

    // The name of the ID column.
    public static final String COLUMN_ID = "id";

    // column constants
    // these are not really needed, but they are added for content provider
    public static final String COLUMN_GUID = "guid";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_PUBLISH_DATE = "pubDate";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_CONTENT = "content";

    // primary key is mandatory
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public Long id;

    // column info annotations are optional
    @ColumnInfo(name = COLUMN_GUID)
    public String guid;

    // column info annotations are optional
    @ColumnInfo(name = COLUMN_TITLE)
    public String title;

    // column info annotations are optional
    @ColumnInfo(name = COLUMN_PUBLISH_DATE)
    public String pubDate;

    // column info annotations are optional
    @ColumnInfo(name = COLUMN_LINK)
    public String link;

    // column info annotations are optional
    @ColumnInfo(name = COLUMN_CONTENT)
    public String content;

    /**
     * Create a new {@link Article} from the specified {@link ContentValues}.
     *
     * This method is needed only for content provider
     *
     * @param values A {@link ContentValues} that contain record values
     * @return A newly created {@link Article} instance.
     */
    public static Article fromContentValues(ContentValues values) {
        final Article article = new Article();

        // fill article fields

        if (values.containsKey(COLUMN_ID)) {
            article.id = values.getAsLong(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_GUID)) {
            article.guid = values.getAsString(COLUMN_GUID);
        }
        if (values.containsKey(COLUMN_TITLE)) {
            article.title = values.getAsString(COLUMN_TITLE);
        }
        if (values.containsKey(COLUMN_PUBLISH_DATE)) {
            article.pubDate = values.getAsString(COLUMN_PUBLISH_DATE);
        }
        if (values.containsKey(COLUMN_LINK)) {
            article.title = values.getAsString(COLUMN_LINK);
        }
        if (values.containsKey(COLUMN_CONTENT)) {
            article.title = values.getAsString(COLUMN_CONTENT);
        }

        return article;
    }

    // helper getter and setter methods to conform with java beans spec

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}