package com.jukkanikki.plainrssreader.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * AppDatabase is singleton for retrieving handle to RoomDatabase instance.
 *
 * See: https://en.wikipedia.org/wiki/Singleton_pattern
 *
 * There's flag to define if database is in memory or persisted to disk.
 *
 * https://en.wikipedia.org/wiki/In-memory_database
 *
 * For this app's use cases both in memory and persisted storage are completely ok.
 * Persistent storage is preferable when offline usage is taken as important requirement.
 * In memory db is faster, and would generally be fine, except when external integrations
 * through content providers and unreliable network connections are considered.
 *
 * Please see: https://developer.android.com/topic/libraries/architecture/room.html
 */
@Database(entities = {Article.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final String ARTICLES_DB = "ARTICLES_DB";

    public static final boolean IN_MEMORY = false;

    /**
     * Single instance of AppDatabase
     */
    private static AppDatabase INSTANCE;

    /**
     * Return article dao
     *
     * @return article dao
     */
    public abstract ArticleDao articleModel();

    /**
     * Create right database based type flag
     *
     * @param context
     * @return
     */
    public static AppDatabase getDatabase(Context context) {
        return IN_MEMORY ? getInMemoryDatabase(context) : getPersistedDatabase(context);
    }


    /**
     * Create in memory db
     *
     * Use this method if storage is needed only during lifecycle of app
     *
     * @param context
     * @return db
     */
    private static AppDatabase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
                            // To simplify the codelab, allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            // TODO: test if needed
                            .allowMainThreadQueries()
                            // destruct previous version - no version migration provided
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    /**
     * Create database
     *
     * Use this method if data should be persisted over restarting app
     *
     * @param context
     * @return db
     */
    private static AppDatabase getPersistedDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, ARTICLES_DB)
                            // To simplify the codelab, allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            // TODO: test if needed
                            .allowMainThreadQueries()
                            // destruct previous version - no version migration provided
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }


    /**
     * Tear down db
     */
    public static void destroyInstance() {
        INSTANCE.close();
        INSTANCE = null;
    }
}
