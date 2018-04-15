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
import shaiytan.ssaplurkreddit.db.Post;

public class CheckedPostsAdapter extends RecyclerView.Adapter<CheckedPostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> data;

    public CheckedPostsAdapter(Context context, List<Post> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_checked_posts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = data.get(position);
        holder.title.setText(post.getTitle());
        Picasso.with(context)
                .load(post.getImageLink())
                .error(R.drawable.not_cashed)
                .into(holder.image);
        int resource = post.isApproved() ? R.drawable.liked : R.drawable.disliked;
        holder.approved.setImageResource(resource);
    }

    @Override
    public int getItemCount() {
        return data.size();
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
