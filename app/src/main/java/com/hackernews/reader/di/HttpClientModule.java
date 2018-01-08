package com.hackernews.reader.di;

import com.hackernews.reader.data.HttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by HJ Chin on 6/1/2018.
 *
 */

@Module
public class HttpClientModule {

    @Provides
    @Singleton
    public HttpClient providesHttpClient(){
        return HttpClient.getInstance();
    }
}
