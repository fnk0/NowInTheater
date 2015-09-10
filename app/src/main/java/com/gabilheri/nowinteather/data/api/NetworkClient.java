package com.gabilheri.nowinteather.data.api;

import android.content.Context;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.gabilheri.nowinteather.BuildConfig;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;

import retrofit.client.Client;
import retrofit.client.OkClient;
import timber.log.Timber;

/**
 * Created by <a href="mailto:marcusandreog@gmail.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 9/2/15.
 */
public class NetworkClient {

    // This is the Cache size that we are going to use for our HttpClient
    private static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

    /**
     * Only used internally
     * Initializes the OkHttpClient and sets up the cache
     *
     * @param context
     *      The context used to get the Android cache directory
     * @return
     *      A new instance of the HttpClient
     */
    private static OkHttpClient initOkHttpClient(Context context) {
        OkHttpClient client = new OkHttpClient();

        if (BuildConfig.DEBUG) {
            client.networkInterceptors().add(new StethoInterceptor());
        }

        // Install an HTTP cache in the application cache directory.
        try {
            File cacheDir = new File(context.getCacheDir(), "http");
            Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
            client.setCache(cache);
        } catch (IOException e) {
            Timber.e(e, "Unable to install disk cache.");
        }

        return client;
    }

    /**
     * To specify the OkHttpClient we need to wrap it in a OkClient first to be used by Retrofit
     *
     * @param context
     *      The context to be passed to the OkHttpClient initializer
     * @return
     *      THe OkClient instance
     *
     */
    public static Client getOkClient(Context context) {
        return new OkClient(initOkHttpClient(context));
    }
}
