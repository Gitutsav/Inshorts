package inshorts.assignment.utsavmoviestmdb.database;


import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import inshorts.assignment.utsavmoviestmdb.model.MovieModel;

@Database(entities = {MovieModel.class}, version = 7, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movielist";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract MovieDao movieDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
            // Create the new table


            database.execSQL(
                    "CREATE TABLE users_new (adult BOOLEAN,\n" +
                            "             backdropPath TEXT,\n" +
                            "             id INTEGER,\n" +
                            "             originalLanguage TEXT,\n" +
                            "             originalTitle TEXT,\n" +
                            "             overview TEXT,\n" +
                            "             popularity DOUBLE,\n" +
                            "             posterPath TEXT,\n" +
                            "             releaseDate TEXT,\n" +
                            "             title TEXT,\n" +
                            "             video BOOLEAN,\n" +
                            "             voteAverage DOUBLE,\n" +
                            "             voteCount INTEGER,\n" +
                            "             movieType INTEGER," +
                            " PRIMARY KEY(id))");
// Copy the data
            database.execSQL(
                    "INSERT INTO users_new (userid, username, last_update) " +
                            "SELECT userid, username, last_update FROM users");
// Remove the old table
            database.execSQL("DROP TABLE users");
// Change the table name to the correct one
            database.execSQL("ALTER TABLE users_new RENAME TO users");


        }
    };
}
