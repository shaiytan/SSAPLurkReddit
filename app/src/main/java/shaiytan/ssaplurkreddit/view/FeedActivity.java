package shaiytan.ssaplurkreddit.view;

import android.content.Intent;
import android.content.SharedPreferences;
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
import shaiytan.ssaplurkreddit.app.LurkRedditApplication;
import shaiytan.ssaplurkreddit.model.RedditAPI;
import shaiytan.ssaplurkreddit.model.RedditPage;
import shaiytan.ssaplurkreddit.model.RedditPost;

public class FeedActivity
        extends AppCompatActivity {
    public static final int LOGIN_REQUEST = 100;

    public static final String SESSION_PREF = "lurk_reddit_session";
    public static final String PREF_LOGIN = "current_login";
    public static final String PREF_ID = "current_id";

    List<RedditPost> liked = new LinkedList<>();
    List<RedditPost> disliked = new LinkedList<>();

    private RecyclerView feed;

    private SharedPreferences preferences;

    private long currentUserId;
    private String currentUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        feed = findViewById(R.id.feed_list);
        feed.setLayoutManager(new LinearLayoutManager(this));
        preferences = getSharedPreferences(SESSION_PREF, MODE_PRIVATE);
        if (preferences.contains(PREF_LOGIN)) {
            loadSession();
            loadFeed();
        } else showLoginForm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_list:
                showCheckedPosts();
                break;
            case R.id.menu_item_logout:
                logOut();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) finish();
        if (resultCode == RESULT_OK) {
            currentUserId = data.getLongExtra(LoginActivity.RESULT_ID, -1);
            currentUserName = data.getStringExtra(LoginActivity.RESULT_LOGIN);
            saveSession();
            setTitle("Hi, " + currentUserName + "! It's AWW subreddit");
            loadFeed();
        }
    }

    private void loadSession() {
        currentUserId = preferences.getLong(PREF_ID, -1);
        currentUserName = preferences.getString(PREF_LOGIN, null);
        setTitle("Hi, " + currentUserName + "! It's AWW subreddit");
    }

    private void saveSession() {
        preferences.edit()
                .putLong(PREF_ID, currentUserId)
                .putString(PREF_LOGIN, currentUserName)
                .apply();
    }

    private void logOut() {
        preferences.edit()
                .remove(PREF_ID)
                .remove(PREF_LOGIN)
                .apply();
        currentUserId = -1;
        currentUserName = null;
        showLoginForm();
    }

    private void showLoginForm() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_REQUEST);
    }

    private void showCheckedPosts() {
        Intent intent = new Intent(this, CheckedPostsActivity.class);
        intent.putExtra(CheckedPostsActivity.LIKED_POSTS, (LinkedList) liked);
        intent.putExtra(CheckedPostsActivity.DISLIKED_POSTS, (LinkedList) disliked);
        startActivity(intent);
    }

    private void loadFeed() {
        RedditAPI api = LurkRedditApplication.getAPI();
        api.getPosts("", 25).enqueue(new Callback<RedditPage>() {
            @Override
            public void onResponse(@NonNull Call<RedditPage> call, @NonNull Response<RedditPage> response) {
                RedditPage page = response.body();
                if (page != null) {
                    LinkedList<RedditPost> posts = new LinkedList<>();
                    for (RedditPost post : page.getChildren()) {
                        if (post.isImage()) posts.add(post);
                    }
                    feed.setAdapter(new PostsAdapter(FeedActivity.this, posts, R.layout.item_feed));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RedditPage> call, @NonNull Throwable t) {
                Toast.makeText(FeedActivity.this, "Cannot load((9:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        new ItemTouchHelper(new SwipeHelper(this::onSwiped)).attachToRecyclerView(feed);
    }

    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        PostsAdapter adapter = (PostsAdapter) feed.getAdapter();
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
}
