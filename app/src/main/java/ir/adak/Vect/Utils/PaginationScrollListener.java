package ir.adak.Vect.Utils;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by delaroy on 12/5/17.
 */

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    private static final String TAG = "MyTagg";
    LinearLayoutManager layoutManager;

    /**
     * Supporting only LinearLayoutManager for now.
     *
     * @param layoutManager
     */
    public PaginationScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        boolean isLoading = IsLoading();
        boolean hasMoreItemsToLoad = HasMoreItemsToLoad();

        if (!IsLoading() && HasMoreItemsToLoad()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                LoadMoreItems();
            }
        }

    }

    protected abstract void LoadMoreItems();

    public abstract boolean IsLoading();

    public abstract boolean HasMoreItemsToLoad();

}