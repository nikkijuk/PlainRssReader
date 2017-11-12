package com.jukkanikki.plainrssreader.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Database is singleton for retrieving handle to RoomDatabase instance
 */
@Database(entities = {Article.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final String ARTICLES_DB = "ARTICLES_DB";

    public static final boolean IN_MEMORY = false;

    private static AppDatabase INSTANCE;

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
         * @return
         */
    private static AppDatabase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
                            // To simplify the codelab, allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            // TODO: test if needed
                            .allowMainThreadQueries()
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
     * @return
     */
    private static AppDatabase getPersistedDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, ARTICLES_DB)
                            // To simplify the codelab, allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            // TODO: test if needed
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }


    public static void destroyInstance() {
        INSTANCE = null;
    }
}
