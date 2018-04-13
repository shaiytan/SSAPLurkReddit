package shaiytan.ssaplurkreddit.app;

import android.app.Application;
import android.arch.persistence.room.Room;

import shaiytan.ssaplurkreddit.db.LurkRedditDatabase;

public class LurkRedditApplication extends Application {
    private static LurkRedditDatabase db;

    public static LurkRedditDatabase getDB() {
        return db;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = Room.databaseBuilder(getApplicationContext(),
                LurkRedditDatabase.class,
                LurkRedditDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build();
    }
}
