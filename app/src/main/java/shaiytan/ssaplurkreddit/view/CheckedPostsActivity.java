package shaiytan.ssaplurkreddit.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import shaiytan.ssaplurkreddit.R;
import shaiytan.ssaplurkreddit.app.LurkRedditApplication;
import shaiytan.ssaplurkreddit.db.Post;
import shaiytan.ssaplurkreddit.db.PostsDAO;
import shaiytan.ssaplurkreddit.view.help.CheckedPostsAdapter;

public class CheckedPostsActivity extends AppCompatActivity {
    public static final String USER_ID = "user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView feed = findViewById(R.id.feed_list);
        feed.setLayoutManager(new LinearLayoutManager(this));
        PostsDAO posts = LurkRedditApplication.getDB().postsDAO();
        long idUser = getIntent().getLongExtra(USER_ID, -1);
        List<Post> postsByUser = posts.getPostsByUser(idUser);
        feed.setAdapter(new CheckedPostsAdapter(this, postsByUser));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
