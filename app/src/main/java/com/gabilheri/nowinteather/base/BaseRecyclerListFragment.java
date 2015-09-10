package com.gabilheri.nowinteather.base;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gabilheri.nowinteather.R;

import butterknife.Bind;

/**
 * Created by <a href="mailto:marcusandreog@gmail.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 9/7/15.
 */
public abstract class BaseRecyclerListFragment extends BaseFragment {

    @Bind(R.id.recyclerview)
    protected RecyclerView mRecyclerView; // Reference to our RecyclerView

    @Nullable
    protected GridLayoutManager mGridLayoutManager;

    @Nullable
    protected LinearLayoutManager mLinearLayoutManager;

    /**
     * Displays a list using a GridLayout with the number of columns specified in our xml file
     * @param adapter
     *       The adapter to be used by this List
     */
    protected void initGridCardsList(RecyclerView.Adapter adapter) {
        int numCols = getResources().getInteger(R.integer.num_cols);
        mGridLayoutManager = new GridLayoutManager(mRecyclerView.getContext(), numCols);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * Displays a list of items 1 item per row
     * @param adapter
     *      The adapter to be used by this list
     */
    protected void initLinearList(RecyclerView.Adapter adapter) {
        mLinearLayoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.list_fragment; // The fragment containing the RecyclerView list
    }
}
