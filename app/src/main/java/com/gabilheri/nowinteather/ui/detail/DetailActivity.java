package com.gabilheri.nowinteather.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;

import com.gabilheri.nowinteather.Const;
import com.gabilheri.nowinteather.MovieUtils;
import com.gabilheri.nowinteather.R;
import com.gabilheri.nowinteather.base.BaseActivity;
import com.gabilheri.nowinteather.data.endpoints.Movie;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by <a href="mailto:marcusandreog@gmail.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 9/7/15.
 */
public class DetailActivity extends BaseActivity {

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @Bind(R.id.item_bg)
    ImageView mItemBackground;

    @Bind(R.id.item_rating_bar)
    AppCompatRatingBar mRatingBar;

    @Bind(R.id.movie_title)
    AppCompatTextView mTitle;

    @Bind(R.id.synopsis)
    AppCompatTextView mSynopsis;

    Movie movie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackNav();
        mCollapsingToolbar.setTitle("");
        setTitle("");
        movie = Parcels.unwrap(getIntent().getExtras().getParcelable(Const.MOVIE));
        loadMovie();
    }

    void loadMovie() {
        loadPicture(movie.getPosters().getOriginal());
        mTitle.setText(movie.getTitle());
        mSynopsis.setText(movie.getSynopsis());
        mRatingBar.setProgress(movie.getRatings().getAudienceScore());
    }

    void loadPicture(String url) {
        Picasso.with(this)
                .load(MovieUtils.getHighResPicUrl(url))
                .error(R.drawable.poster_default_thumb)
                .fit()
                .centerCrop()
                .into(mItemBackground);
    }

    @OnClick(R.id.fab_share)
    public void share(View v) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this movie!!\n" + movie.getLinks().getAlternate());
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share Movie"));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_detail;
    }
}
