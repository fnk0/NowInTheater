package com.gabilheri.nowinteather;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.gabilheri.nowinteather.data.api.Api;
import com.gabilheri.nowinteather.data.api.ApiClient;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

/**
 * Created by <a href="mailto:marcusandreog@gmail.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 9/7/15.
 */
public class MoviesApp extends Application {

    static MoviesApp mInstance;
    Api mApi;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                            .build());
        }
    }

    public static MoviesApp instance() {
        return mInstance;
    }



    /**
     * If the api is null them we make a new instance
     * Otherwise we just return it
     * This technique is called Lazy Instantiation allowing us to only instantiate singleton
     * Objects when we actually need them rather than keeping objects not used in Memory
     *
     * @return
     *      A instance of the Api
     */
    public Api api() {
        if(mApi == null) {
            mApi = ApiClient.initApi(this);
        }
        return mApi;
    }
}
