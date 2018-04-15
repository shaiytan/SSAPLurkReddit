package shaiytan.ssaplurkreddit.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PostsDAO {
    @Insert
    long insertPost(Post post);

    @Query("SELECT * FROM posts WHERE id_user = :user")
    List<Post> getPostsByUser(long user);

    @Query("SELECT id_reddit FROM posts WHERE id_user = :user")
    List<String> getViewedPostsId(long user);
}
