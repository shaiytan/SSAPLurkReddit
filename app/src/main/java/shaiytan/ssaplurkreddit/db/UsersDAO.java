package shaiytan.ssaplurkreddit.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.sqlite.SQLiteException;

@Dao
public interface UsersDAO {
    @Insert
    long insertUser(User user) throws SQLiteException;

    @Query("SELECT * FROM users WHERE login = :login")
    User findUserByLogin(String login);
}
