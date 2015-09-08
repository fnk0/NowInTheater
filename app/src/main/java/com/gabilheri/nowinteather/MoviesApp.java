package com.gabilheri.nowinteather;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.gabilheri.nowinteather.data.api.Api;
import com.gabilheri.nowinteather.data.api.ApiClient;

import timber.log.Timber;

/**
 * Created by <a href="mailto:marcusandreog@gmail.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 9/7/15.
 */
public class MoviesApp extends Application {

    private static MoviesApp mInstance;
    private Api mApi;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

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

    public Api api() {
        if(mApi == null) {
            mApi = ApiClient.initApi(this);
        }
        return mApi;
    }
}
