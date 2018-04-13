package shaiytan.ssaplurkreddit.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {User.class}, version = 1)
public abstract class LurkRedditDatabase extends RoomDatabase {

    public static final String DB_NAME = "lurkreddit.db";

    public abstract UsersDAO usersDAO();
}
