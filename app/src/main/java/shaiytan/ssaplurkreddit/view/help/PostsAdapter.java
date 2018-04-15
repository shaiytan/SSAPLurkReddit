package shaiytan.ssaplurkreddit.view.help;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import shaiytan.ssaplurkreddit.R;
import shaiytan.ssaplurkreddit.model.RedditPost;

public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int REGULAR_ITEM_TYPE = 0;
    private static final int LOAD_MORE_TYPE = 1;

    private Context context;
    private List<RedditPost> data;
    private View.OnClickListener loadListener;

    public PostsAdapter(Context context, List<RedditPost> data, View.OnClickListener loadListener) {
        this.context = context;
        this.data = data;
        this.loadListener = loadListener;
    }

    public static PostsAdapter empty(Context context, View.OnClickListener listener) {
        return new PostsAdapter(context, new ArrayList<>(), listener);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == REGULAR_ITEM_TYPE) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_feed, parent, false);
            return new ViewHolder(view);
        } else if (viewType == LOAD_MORE_TYPE) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_load, parent, false);
            return new BottomViewHolder(view, loadListener);
        } else throw new IllegalArgumentException("Invalid viewType!");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            RedditPost post = data.get(position);
            ViewHolder regularHolder = (ViewHolder) holder;
            regularHolder.title.setText(post.getTitle());
            Picasso.with(context)
                    .load(post.getImageLink())
                    .into(regularHolder.image);
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    public RedditPost pullItem(int position) {
        RedditPost post = data.get(position);
        data.remove(position);
        notifyItemRemoved(position);
        return post;
    }

    @Override
    public int getItemViewType(int position) {
        return position == data.size() ? LOAD_MORE_TYPE : REGULAR_ITEM_TYPE;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            image = itemView.findViewById(R.id.item_image);
        }
    }

    public void insertItems(List<RedditPost> items) {
        data.addAll(items);
        notifyDataSetChanged();
    }

    static class BottomViewHolder extends RecyclerView.ViewHolder {
        BottomViewHolder(View itemView, View.OnClickListener listener) {
            super(itemView);
            Button loadMore = itemView.findViewById(R.id.btn_load_more);
            loadMore.setOnClickListener(listener);
        }
    }

    public static PostsAdapter error(Context context) {
        return new ErrorAdapter(context);
    }

    private static class ErrorAdapter extends PostsAdapter {
        private ErrorAdapter(Context context) {
            super(context, null, null);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ViewHolder regularHolder = (ViewHolder) holder;
            regularHolder.image.setImageResource(R.drawable.no_internet);
            regularHolder.title.setText(R.string.no_internet_message);
        }

        @Override
        public int getItemCount() {
            return 1;
        }

        @Override
        public int getItemViewType(int position) {
            return REGULAR_ITEM_TYPE;
        }

        @Override
        public RedditPost pullItem(int position) {
            return null;
        }
    }
}
