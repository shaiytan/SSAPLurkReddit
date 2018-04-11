package shaiytan.ssaplurkreddit.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.LinkedList;
import java.util.List;

import shaiytan.ssaplurkreddit.R;
import shaiytan.ssaplurkreddit.model.RedditPost;

public class CheckedPostsActivity extends AppCompatActivity {

    public static final String LIKED_POSTS = "likedposts";
    public static final String DISLIKED_POSTS = "dislikedposts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView feed = findViewById(R.id.feed_list);
        feed.setLayoutManager(new LinearLayoutManager(this));
        List<RedditPost> likedPosts = (List<RedditPost>) getIntent().getSerializableExtra(LIKED_POSTS);
        List<RedditPost> dislikedPosts = (List<RedditPost>) getIntent().getSerializableExtra(DISLIKED_POSTS);
        List<RedditPost> all = new LinkedList<>(likedPosts);
        all.addAll(dislikedPosts);
        feed.setAdapter(new FeedAdapter(this, all, R.layout.item_checked_posts));
    }

}
