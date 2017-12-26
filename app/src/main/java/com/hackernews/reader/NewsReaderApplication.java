package com.hackernews.reader;

import android.app.Application;

import com.hackernews.reader.di.AppComponent;
import com.hackernews.reader.di.DaggerAppComponent;
import com.hackernews.reader.data.ProviderModule;


/**
 * Created by HJ Chin on 23/12/2017.
 */

public class NewsReaderApplication extends Application {

    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent
                .builder()
                .providerModule(new ProviderModule())
                .build();
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }
}
