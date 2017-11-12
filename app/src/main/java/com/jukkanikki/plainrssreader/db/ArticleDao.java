package com.jukkanikki.plainrssreader.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

/**
 * Dao for manipulating article entities
 *
 * All data access operations for article are described here with annotations
 *
 * Please see: https://developer.android.com/training/data-storage/room/accessing-data.html
 */
@Dao
public interface ArticleDao {

    /**
     * get number of articles
     *
     * @return count of articles
     */
    @Query("select count(*) from "+Article.TABLE_NAME)
    Long countArticles();

    /**
     * get all articles
     *
     * @return list of articles
     */
    @Query("select * from "+Article.TABLE_NAME)
    List<Article> allArticles();

    /**
     * return all articles as cursor
     *
     * @return cursor to all articles
     */
    @Query("select * from "+Article.TABLE_NAME)
    Cursor cursorAllArticles();

    /**
     * cursor to exact article identified with id
     *
     * @param id identifier of article
     * @return article
     */
    @Query("select * from "+Article.TABLE_NAME+" where "+Article.COLUMN_ID +" = :id")
    Cursor cursorExactArticle(String id);

    /**
     * Insert article
     * @param article article to be inserted
     * @return id of inserted article
     */
    @Insert(onConflict = IGNORE)
    long insertArticle(Article article);

    /**
     * Delete article
     *
     * @param article article to be deleted
     */
    @Delete
    void deleteArticle(Article article);

    /**
     * Delete article identified with id
     *
     * @param id id of article
     * @return number of deleted entities
     */
    @Query("delete from "+Article.TABLE_NAME+" where "+Article.COLUMN_ID +" = :id")
    int deleteById(long id);

    /**
     * Batch insert of articles
     *
     * @param articles array of articles to be inserted
     */
    @Insert(onConflict = IGNORE)
    void insertOrReplaceArticles(Article... articles);

    /**
     * Update article
     *
     * @param article article to be modified
     * @return amound of entities modified
     */
    @Update
    int updateArticle(Article article);

    /**
     * Delete all articles
     */
    @Query("delete from "+Article.TABLE_NAME)
    void deleteAll();
}
