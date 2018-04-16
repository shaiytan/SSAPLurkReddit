package shaiytan.ssaplurkreddit.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shaiytan.ssaplurkreddit.R;
import shaiytan.ssaplurkreddit.app.LurkRedditApplication;
import shaiytan.ssaplurkreddit.db.Post;
import shaiytan.ssaplurkreddit.db.PostsDAO;
import shaiytan.ssaplurkreddit.model.RedditAPI;
import shaiytan.ssaplurkreddit.model.RedditPage;
import shaiytan.ssaplurkreddit.model.RedditPost;
import shaiytan.ssaplurkreddit.view.help.PostsAdapter;
import shaiytan.ssaplurkreddit.view.help.SwipeHelper;

public class FeedActivity
        extends AppCompatActivity {
    public static final int LOGIN_REQUEST = 100;

    public static final String SESSION_PREF = "lurk_reddit_session";
    public static final String PREF_LOGIN = "current_login";
    public static final String PREF_ID = "current_id";

    private RecyclerView feed;
    private PostsAdapter feedAdapter;
    private PostsAdapter errorAdapter;
    private ItemTouchHelper swipeHelper;
    private SwipeRefreshLayout pullRefresh;
    private ImageView plus;
    private ImageView minus;

    private SharedPreferences preferences;
    private PostsDAO postsDAO;

    private long currentUserId;
    private String currentUserName;
    private String nextPage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        if (preferences.contains(PREF_LOGIN)) {
            loadSession();
            refreshFeed();
        } else showLoginForm();
    }

    private void init() {
        plus = findViewById(R.id.img_plus);
        minus = findViewById(R.id.img_minus);
        errorAdapter = PostsAdapter.error(this);
        feed = findViewById(R.id.feed_list);
        feed.setLayoutManager(new LinearLayoutManager(this));
        swipeHelper = new ItemTouchHelper(new SwipeHelper(this::onSwiped));
        pullRefresh = findViewById(R.id.refresh);
        pullRefresh.setOnRefreshListener(this::refreshFeed);
        postsDAO = LurkRedditApplication.getDB().postsDAO();
        preferences = getSharedPreferences(SESSION_PREF, MODE_PRIVATE);
    }

    private void showLoginForm() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) finish();
        if (resultCode == RESULT_OK) {
            currentUserId = data.getLongExtra(LoginActivity.RESULT_ID, -1);
            currentUserName = data.getStringExtra(LoginActivity.RESULT_LOGIN);
            saveSession();
            setTitle("@" + currentUserName + ":AWW subreddit");
            refreshFeed();
        }
    }

    // manage session methods
    private void loadSession() {
        currentUserId = preferences.getLong(PREF_ID, -1);
        currentUserName = preferences.getString(PREF_LOGIN, null);
        setTitle("@" + currentUserName + ":AWW subreddit");
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

    private void refreshFeed() {
        nextPage = "";
        feedAdapter = PostsAdapter.empty(this, v -> loadFeed());
        pullRefresh.setRefreshing(true);
        loadFeed();
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
                Intent intent = new Intent(this, CheckedPostsActivity.class);
                intent.putExtra(CheckedPostsActivity.USER_ID, currentUserId);
                startActivity(intent);
                break;
            case R.id.menu_item_logout:
                logOut();
                break;
        }
        return true;
    }

    private void loadFeed() {
        RedditAPI api = LurkRedditApplication.getAPI();
        api.getPosts(nextPage, 25).enqueue(new Callback<RedditPage>() {
            @Override
            public void onResponse(@NonNull Call<RedditPage> call, @NonNull Response<RedditPage> response) {
                RedditPage page = response.body();
                if (page != null) {
                    showLoadedPosts(page);
                }
                pullRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<RedditPage> call, @NonNull Throwable t) {
                swipeHelper.attachToRecyclerView(null);
                feed.setAdapter(errorAdapter);
                pullRefresh.setRefreshing(false);
                plus.setVisibility(View.INVISIBLE);
                minus.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void showLoadedPosts(RedditPage page) {
        nextPage = page.getAfter();
        Set<String> viewed = new HashSet<>(postsDAO.getViewedPostsId(currentUserId));
        // filter posts - don't show posts that were checked by user
        // and show only posts with images
        List<RedditPost> loadedPosts = page.getChildren();
        List<RedditPost> filteredPosts = new LinkedList<>();
        for (RedditPost post : loadedPosts) {
            if (post.isImage() && !viewed.contains(post.getId()))
                filteredPosts.add(post);
        }
        feedAdapter.insertItems(filteredPosts);
        if (feed.getAdapter() != feedAdapter) feed.setAdapter(feedAdapter);
        swipeHelper.attachToRecyclerView(feed);
        plus.setVisibility(View.VISIBLE);
        minus.setVisibility(View.VISIBLE);
        // load at least 5 posts
        // except the case when user is a nolifer-bitard and he loaded whole subreddit
        if (filteredPosts.size() < 5 && filteredPosts.size() < loadedPosts.size())
            loadFeed();
    }

    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        PostsAdapter adapter = (PostsAdapter) feed.getAdapter();
        RedditPost post = adapter.pullItem(viewHolder.getAdapterPosition());
        boolean approved = direction == ItemTouchHelper.RIGHT;
        Post savePost = new Post(
                post.getId(),
                currentUserId,
                post.getTitle(),
                post.getImageLink(),
                approved
        );
        Toast.makeText(this, approved ? "Liked" : "Disliked", Toast.LENGTH_SHORT).show();
        postsDAO.insertPost(savePost);
    }
}
