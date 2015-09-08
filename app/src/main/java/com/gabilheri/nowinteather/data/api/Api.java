package com.gabilheri.nowinteather.data.api;

import com.gabilheri.nowinteather.data.endpoints.Movies;

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

    @GET("/lists/movies/in_theaters.json")
    Observable<Movies> getMovies(
            @Query("apikey") String key,
            @Query("page") int page,
            @Query("page_limit") int page_limit
    );

}
