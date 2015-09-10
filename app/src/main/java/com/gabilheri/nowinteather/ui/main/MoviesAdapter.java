package com.gabilheri.nowinteather.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gabilheri.nowinteather.MovieUtils;
import com.gabilheri.nowinteather.R;
import com.gabilheri.nowinteather.base.ItemCallback;
import com.gabilheri.nowinteather.data.endpoints.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by <a href="mailto:marcusandreog@gmail.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 9/7/15.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MovieHolder> {

    ItemCallback<View> mCallback;
    List<Movie> movies;

    public MoviesAdapter(List<Movie> movies, ItemCallback callback) {
        this.movies = movies;
        this.mCallback = callback;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieHolder(view, mCallback);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.ratingBar.setProgress(movie.getRatings().getAudienceScore());
        holder.movieTitle.setText(movie.getTitle());

        Picasso.with(holder.itemView.getContext())
                .load(MovieUtils.getHighResPicUrl(movie.getPosters().getThumbnail()))
                .error(R.drawable.poster_default_thumb)
                .fit()
                .centerCrop()
                .into(holder.itemThumbnail);

        holder.itemView.setTag(R.id.movie, movie);
    }

    public void addAll(List<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public List<Movie> getMovies() {
        return movies;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
