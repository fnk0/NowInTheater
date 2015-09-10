package com.gabilheri.nowinteather.ui.main;

import android.support.annotation.NonNull;
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

    /**
     *
     * @param movies
     *      The list of movies to be used by This Adapter
     * @param callback
     *      The callback that will receive a call when a Item is clicked on the list
     */
    public MoviesAdapter(@NonNull List<Movie> movies, @NonNull ItemCallback<View> callback) {
        this.movies = movies;
        this.mCallback = callback;
    }

    // onCreateViewHolder only gets called when we are instantiating a new View
    // When we are recycling views this will not be called
    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflates our .xml layout that we are going to be used by our ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieHolder(view, mCallback); // Returns a new instance of the ViewHolder
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        Movie movie = movies.get(position); // Retrieve the Movie from our list

        holder.ratingBar.setProgress(movie.getRatings().getAudienceScore());
        holder.movieTitle.setText(movie.getTitle());

        Picasso.with(holder.itemView.getContext())
                .load(MovieUtils.getHighResPicUrl(movie.getPosters().getThumbnail())) // Loads the Thumbnail
                .error(R.drawable.poster_default_thumb) // We fall back to the default Thumbnail in case of page load
                .fit() // Resizes the image to fit the ImageView and save memory
                .centerCrop() // Centers the image in the ImageView and crop extra elements
                .into(holder.itemThumbnail); // The ImageView on which load the image

        // The Tag object will be used by our onItemClick to pass data from the Adapter to the Fragment
        holder.itemView.setTag(R.id.movie, movie);
    }

    /**
     * Adds all the items to our current list (usually when we are finished loading a page)
     * @param movies
     *      The list of movies to be added to this Adapter
     */
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
