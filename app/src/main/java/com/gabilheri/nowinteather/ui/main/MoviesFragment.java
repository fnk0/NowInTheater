package com.gabilheri.nowinteather.ui.main;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.gabilheri.nowinteather.Const;
import com.gabilheri.nowinteather.MoviesApp;
import com.gabilheri.nowinteather.R;
import com.gabilheri.nowinteather.base.BaseRecyclerListFragment;
import com.gabilheri.nowinteather.base.ItemCallback;
import com.gabilheri.nowinteather.base.RxCallback;
import com.gabilheri.nowinteather.base.RxSubscriber;
import com.gabilheri.nowinteather.data.endpoints.Movie;
import com.gabilheri.nowinteather.data.endpoints.Movies;
import com.gabilheri.nowinteather.ui.OnScrolledCallback;
import com.gabilheri.nowinteather.ui.ScrollListener;
import com.gabilheri.nowinteather.ui.detail.DetailActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by <a href="mailto:marcusandreog@gmail.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 9/7/15.
 */
public class MoviesFragment extends BaseRecyclerListFragment
        implements ItemCallback, RxCallback<Movies>, OnScrolledCallback {

    MoviesAdapter mAdapter;
    List<Movie> mItems;
    int mPosition;

    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mItems = new ArrayList<>();

        if (savedInstanceState != null) {
            mItems = Parcels.unwrap(savedInstanceState.getParcelable(Const.MOVIE));
            mPosition = savedInstanceState.getInt(Const.LIST_POSITION);
            mRecyclerView.smoothScrollToPosition(mPosition);
        }

        mAdapter = new MoviesAdapter(mItems, this);
        initGridCardsList(mAdapter);
        mRecyclerView.addOnScrollListener(new ScrollListener(mGridLayoutManager, this));

        if (mItems.size() == 0) {
            getMovies(1);
        }
    }

    @Override
    public void onItemClick(View v) {
        ImageView imageView = (ImageView) v.findViewById(R.id.item_image);
        Movie movie = (Movie) v.getTag(R.id.movie);
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(Const.MOVIE, Parcels.wrap(movie));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), imageView, Const.TRANSITION_IMAGE);
            getActivity().startActivity(intent, options.toBundle());
        } else {
            getActivity().startActivity(intent);
        }
    }

    void getMovies(int page) {
        MoviesApp.instance().api().getMovies(Const.API_KEY, page, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<>(this));
    }

    @Override
    public void onDataReady(Movies data) {
        mAdapter.addAll(data.getMovies());
    }

    @Override
    public void onDataError(Throwable e) {
        Timber.e(e, "Error downloading movie data");
    }

    @Override
    public void onScrolled(int page) {
        getMovies(page);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Const.MOVIE, Parcels.wrap(mItems));
        outState.putInt(Const.LIST_POSITION, mGridLayoutManager.findFirstVisibleItemPosition());
    }
}
