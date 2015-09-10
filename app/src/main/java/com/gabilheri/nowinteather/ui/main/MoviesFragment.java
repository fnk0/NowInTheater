package com.gabilheri.nowinteather.ui.main;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.gabilheri.nowinteather.data.endpoints.MovieResponse;
import com.gabilheri.nowinteather.ui.OnScrolledCallback;
import com.gabilheri.nowinteather.ui.ScrollListener;
import com.gabilheri.nowinteather.ui.detail.DetailActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by <a href="mailto:marcusandreog@gmail.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 9/7/15.
 */
public class MoviesFragment extends BaseRecyclerListFragment
        implements ItemCallback<View>, RxCallback<MovieResponse>, OnScrolledCallback {

    MoviesAdapter mAdapter;
    List<Movie> mItems;
    ScrollListener mScrollListener;
    int mCurrentPage = 1;

    /**
     * Gets a new instance of this fragment. This is specially important
     * if you have Bundle arguments
     *
     * @return
     *      a New instance of a MoviesFragment
     */
    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mItems = new ArrayList<>();

        // This will not be null if the device was rotated or if the user
        // hit the Home button and came back to our apps
        if (savedInstanceState != null) {
            mItems = Parcels.unwrap(savedInstanceState.getParcelable(Const.MOVIE));
            mRecyclerView.smoothScrollToPosition(savedInstanceState.getInt(Const.LIST_POSITION));
            mCurrentPage = savedInstanceState.getInt(Const.CURRENT_PAGE);
        }

        // Note: this == OnScrolledCallback
        mAdapter = new MoviesAdapter(mItems, this);

        // Instantiates a GridList
        initGridCardsList(mAdapter);

        mScrollListener = new ScrollListener(mGridLayoutManager, this);
        mScrollListener.setTotalItemCount(mItems.size());
        mScrollListener.setCurrentPage(mCurrentPage);

        mRecyclerView.addOnScrollListener(mScrollListener);

        // If we have 0 items this was not loaded from the savedInstanceState
        // So we retrieve the 1st page of movies
        if (mItems.size() == 0) {
            getMovies(1);
        }
    }

    @Override
    public void onItemClick(View v) {
        // v is the itemView from our Adapter

        // Retrieve our MovieObject from the Tag set to the ItemView
        Movie movie = (Movie) v.getTag(R.id.movie);

        // Create a new intent
        Intent intent = new Intent(getActivity(), DetailActivity.class);

        // Add the Movie to the extras  bundle
        intent.putExtra(Const.MOVIE, Parcels.wrap(movie));

        // If we are in Lollipop we are going to have a cool transition Animation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Gets a reference to our ImageView
            ImageView imageView = ButterKnife.findById(v, R.id.item_image);
            // This will animate the transition in between the list and the DetailActivity
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), imageView, Const.TRANSITION_IMAGE);
            getActivity().startActivity(intent, options.toBundle());
        } else {
            // Otherwise we just Start this activity
            getActivity().startActivity(intent);
        }
    }

    /**
     * Retrieves a list of 10 movies from the Rotten tomatoes API and starts a new Subscriber to
     * receive the data
     *
     * @param page
     *      The page we want to retrieve
     */
    void getMovies(int page) {
        mCurrentPage = page;
        // Gets a reference to our API
        MoviesApp.instance().api()
                // This returns a Observable<MovieResponse> from our API
                .getMovies(Const.API_KEY, mCurrentPage, 10)
                // We tell Android that we want to perform this task in the Schedulers.io() thread
                // You could also spawn a new thread here if you wish
                .subscribeOn(Schedulers.io())
                // We tell Android that the results will be passed to the MainThread
                // This allows us to update the UI
                .observeOn(AndroidSchedulers.mainThread())
                // We make a new Subscriber that will receive the data inside onNext
                .subscribe(new RxSubscriber<>(this));
    }

    @Override
    public void onDataReady(MovieResponse data) {
        // This gets called from our RxCallback<MovieResponse>
        // All we need to do is call our Adapter method do add all items
        mAdapter.addAll(data.getMovies());
    }

    @Override
    public void onDataError(Throwable e) {
        // If we have a error downloading the data we tell the user with a SnackBar
        if (getView() != null) {
            Snackbar.make(getView(), "Error downloading the Movie data. Try again later", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onScrolled(int page) {
        // This gets called from our onScrolledCallback
        getMovies(page);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // We are going to save the List of movies when the device rotates or the activity
        // goes into the background. This will ensure that we don't have to make redundant network calls
        // And also gives a better experience to the user
        outState.putParcelable(Const.MOVIE, Parcels.wrap(mItems));

        if (mScrollListener != null) {
            outState.putInt(Const.CURRENT_PAGE, mCurrentPage);
        }

        if(mGridLayoutManager != null) {
            // We save the current List position so when we come back from rotation we are
            // in the same position.
            outState.putInt(Const.LIST_POSITION, mGridLayoutManager.findFirstVisibleItemPosition());
        }
    }
}
