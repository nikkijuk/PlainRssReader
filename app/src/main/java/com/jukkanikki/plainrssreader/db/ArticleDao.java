package com.jukkanikki.plainrssreader.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

/**
 * Dao for manipulating article
 *
 * All data access operations for article are described here with annotations
 */
@Dao
public interface ArticleDao {
    @Query("select count(*) from "+Article.TABLE_NAME)
    Long countArticles();

    @Query("select * from "+Article.TABLE_NAME)
    List<Article> allArticles();

    @Query("select * from "+Article.TABLE_NAME)
    Cursor cursorAllArticles();

    @Query("select * from "+Article.TABLE_NAME+" where "+Article.COLUMN_ID+" = :id")
    Cursor cursorExactArticle(String id);

    @Insert(onConflict = IGNORE)
    void insertArticle(Article article);

    @Delete
    void deleteArticle(Article article);

    @Insert(onConflict = IGNORE)
    void insertOrReplaceArticles(Article... articles);

    @Query("delete from "+Article.TABLE_NAME)
    void deleteAll();
}
