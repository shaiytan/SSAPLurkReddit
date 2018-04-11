package shaiytan.ssaplurkreddit.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shaiytan.ssaplurkreddit.R;
import shaiytan.ssaplurkreddit.model.RedditAPI;
import shaiytan.ssaplurkreddit.model.RedditPage;
import shaiytan.ssaplurkreddit.model.RedditPost;

public class FeedActivity extends AppCompatActivity {

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
                    feed.setAdapter(new FeedAdapter(FeedActivity.this, posts));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RedditPage> call, @NonNull Throwable t) {
                Toast.makeText(FeedActivity.this, "Cannot load((9:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());
    }

}
