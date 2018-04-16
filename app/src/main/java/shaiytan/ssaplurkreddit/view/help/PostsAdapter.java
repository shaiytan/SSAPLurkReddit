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
    static final int REGULAR_ITEM_TYPE = 0;
    static final int LOAD_MORE_TYPE = 1;

    private Context context;
    private List<RedditPost> data;
    private View.OnClickListener loadListener;

    PostsAdapter(Context context, List<RedditPost> data, View.OnClickListener loadListener) {
        this.context = context;
        this.data = data;
        this.loadListener = loadListener;
    }

    //factory methods for adapters
    public static PostsAdapter empty(Context context, View.OnClickListener listener) {
        return new PostsAdapter(context, new ArrayList<>(), listener);
    }

    public static PostsAdapter error(Context context) {
        return new ErrorAdapter(context);
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == REGULAR_ITEM_TYPE) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_feed, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == LOAD_MORE_TYPE) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_load, parent, false);
            return new LoadMoreViewHolder(view, loadListener);
        } else throw new IllegalArgumentException("Invalid viewType!");
    }

    @Override
    public int getItemViewType(int position) {
        return position == data.size() ? LOAD_MORE_TYPE : REGULAR_ITEM_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            RedditPost post = data.get(position);
            ItemViewHolder regularHolder = (ItemViewHolder) holder;
            regularHolder.title.setText(post.getTitle());
            Picasso.with(context)
                    .load(post.getImageLink())
                    .into(regularHolder.image);
        }
    }

    public void insertItems(List<RedditPost> items) {
        data.addAll(items);
        notifyDataSetChanged();
    }

    //view holder for regular items - elements of reddit posts list
    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView image;

        ItemViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            image = itemView.findViewById(R.id.item_image);
        }
    }

    //view holder for the last item - "load more" button
    private static class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        LoadMoreViewHolder(View itemView, View.OnClickListener listener) {
            super(itemView);
            Button loadMore = itemView.findViewById(R.id.btn_load_more);
            loadMore.setOnClickListener(listener);
        }
    }

    //special adapter that is used when app cannot load data (network unavailable)
    private static class ErrorAdapter extends PostsAdapter {
        private ErrorAdapter(Context context) {
            super(context, null, null);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ItemViewHolder regularHolder = (ItemViewHolder) holder;
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
