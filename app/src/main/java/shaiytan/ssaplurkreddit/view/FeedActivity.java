package shaiytan.ssaplurkreddit.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shaiytan.ssaplurkreddit.R;
import shaiytan.ssaplurkreddit.model.RedditAPI;
import shaiytan.ssaplurkreddit.model.RedditPage;
import shaiytan.ssaplurkreddit.model.RedditPost;

public class FeedActivity extends AppCompatActivity {
    List<RedditPost> liked = new LinkedList<>();
    List<RedditPost> disliked = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView feed = findViewById(R.id.feed_list);
        feed.setLayoutManager(new LinearLayoutManager(this));
        RedditAPI api = RedditAPI.getAPI();
        api.getPosts("", 25).enqueue(new Callback<RedditPage>() {
            @Override
            public void onResponse(@NonNull Call<RedditPage> call, @NonNull Response<RedditPage> response) {
                RedditPage page = response.body();
                if (page != null) {
                    LinkedList<RedditPost> posts = new LinkedList<>();
                    for (RedditPost post : page.getChildren()) {
                        if (post.isImage()) posts.add(post);
                    }
                    feed.setAdapter(new FeedAdapter(FeedActivity.this, posts, R.layout.item_feed));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RedditPage> call, @NonNull Throwable t) {
                Toast.makeText(FeedActivity.this, "Cannot load((9:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                FeedAdapter adapter = (FeedAdapter) feed.getAdapter();
                RedditPost post = adapter.pullItem(viewHolder.getAdapterPosition());
                List<RedditPost> list;
                if (direction == ItemTouchHelper.RIGHT) {
                    list = liked;
                    post.approve(true);
                } else {
                    list = disliked;
                    post.approve(false);
                }
                list.add(post);
                Toast.makeText(FeedActivity.this,
                        "Liked:" + liked.size() + ", Disliked:" + disliked.size(),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        };
        new ItemTouchHelper(callback).attachToRecyclerView(feed);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, CheckedPostsActivity.class);
        intent.putExtra(CheckedPostsActivity.LIKED_POSTS, (LinkedList) liked);
        intent.putExtra(CheckedPostsActivity.DISLIKED_POSTS, (LinkedList) disliked);
        startActivity(intent);
        return true;
    }
}
