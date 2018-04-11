package shaiytan.ssaplurkreddit.view;

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

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    private Context context;
    private List<RedditPost> data;
    private int layout;

    public FeedAdapter(Context context, List<RedditPost> data, int layout) {
        this.context = context;
        this.data = data;
        this.layout = layout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RedditPost post = data.get(position);
        holder.title.setText(post.getTitle());
        Picasso.with(context)
                .load(post.getImageLink())
                .into(holder.image);
        if (holder.approved != null) {
            int resource = post.getRate() ? R.drawable.liked : R.drawable.disliked;
            holder.approved.setImageResource(resource);
        }

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
        private ImageView approved;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            image = itemView.findViewById(R.id.item_image);
            approved = itemView.findViewById(R.id.item_approved);
        }
    }
}
