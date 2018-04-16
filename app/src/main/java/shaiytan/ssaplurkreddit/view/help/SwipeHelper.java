package shaiytan.ssaplurkreddit.view.help;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SwipeHelper extends ItemTouchHelper.SimpleCallback {
    private SwipeListener listener;

    public SwipeHelper(SwipeListener listener) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //don't swipe "load more" button
        if (viewHolder.getItemViewType() == PostsAdapter.LOAD_MORE_TYPE) return 0;
        else return super.getSwipeDirs(recyclerView, viewHolder);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (viewHolder.getItemViewType() == PostsAdapter.LOAD_MORE_TYPE) return;
        listener.onSwiped(viewHolder, direction);
    }

    public interface SwipeListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction);
    }
}
