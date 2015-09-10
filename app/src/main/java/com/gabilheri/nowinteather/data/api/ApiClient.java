package com.gabilheri.nowinteather.data.api;

import android.app.Application;

import com.gabilheri.nowinteather.BuildConfig;

import retrofit.RestAdapter;

/**
 * Created by <a href="mailto:marcusandreog@gmail.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 9/7/15.
 */
public class ApiClient {

    public static final String API_VERSION = "v1.0";
    public static final String API_BASE_URL = "http://api.rottentomatoes.com/api/public";

    /**
     * Using the Application class which is a Singleton by nature guarantees this client to be a
     * singleton. Will also keep the activities from holding references to it avoiding memory leaks
     *
     * @param application
     *      Application on which this client should be initialized
     * @return
     *      The Api on which to make calls
     */
    public static Api initApi(Application application) {
        return new RestAdapter.Builder()
                // Sets the base Endpoint of this client
                .setEndpoint(buildApiEndpointURL())
                // If we are in a Debug build them we set the LogLevel to FULL
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                // The HttpClient that we are going to use to by the Api
                .setClient(NetworkClient.getOkClient(application))
                // Builds the RestAdapter that we is going to be used to create the API
                .build()
                // Creates the API
                // Note: this should be the Api.class that we made
                .create(Api.class);
    }

    /**
     * @return
     *      The string with our Endpoint URL
     */
    private static String buildApiEndpointURL() {
        return String.format("%s/%s", API_BASE_URL, API_VERSION);
    }
}
