package com.gabilheri.nowinteather.data.api;

import com.gabilheri.nowinteather.data.endpoints.MovieResponse;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by <a href="mailto:marcusandreog@gmail.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 9/7/15.
 */
public interface Api {

    /**
     *
     * A lot of Magic is going on here... First of all we specify Retrofit that we are going to make
     * a GET request to the API with 3 Query Parameters
     *
     * Retrofit makes the Call to the API and parses the GSON into our MovieResponse
     *
     * @param key
     *      The Api Key for the API
     * @param page
     *      The page that we want to retrieve data from
     * @param page_limit
     *      The page limit of items
     * @return
     *      A RxJava Observable with the MovieResponse from the API
     */
    @GET("/lists/movies/in_theaters.json")
    Observable<MovieResponse> getMovies(
            @Query("apikey") String key,
            @Query("page") int page,
            @Query("page_limit") int page_limit
    );
}
