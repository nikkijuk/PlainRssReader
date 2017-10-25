package com.jukkanikki.plainrssreader.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface ArticleDao {
    @Query("select * from article")
    List<Article> loadAllArticles();

    @Insert(onConflict = IGNORE)
    void insertArticle(Article article);

    @Delete
    void deleteArticle(Article article);

    @Insert(onConflict = IGNORE)
    void insertOrReplaceArticles(Article... articles);

    @Query("delete from article")
    void deleteAll();
}
