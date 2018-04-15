package shaiytan.ssaplurkreddit.view.help;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import shaiytan.ssaplurkreddit.R;
import shaiytan.ssaplurkreddit.model.RedditPost;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<RedditPost> data;

    public PostsAdapter(Context context, List<RedditPost> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RedditPost post = data.get(position);
        holder.title.setText(post.getTitle());
        Picasso.with(context)
                .load(post.getImageLink())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public RedditPost pullItem(int position) {
        RedditPost post = data.get(position);
        data.remove(position);
        notifyItemRemoved(position);
        return post;
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

    public static PostsAdapter error(Context context) {
        return new ErrorAdapter(context);
    }

    private static class ErrorAdapter extends PostsAdapter {
        private ErrorAdapter(Context context) {
            super(context, null);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.image.setImageResource(R.drawable.no_internet);
            holder.title.setText(R.string.no_internet_message);
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }
}
