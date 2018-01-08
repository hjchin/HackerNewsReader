package com.hackernews.reader.data;


import retrofit2.Retrofit;

/**
 * Created by HJ Chin on 5/12/2017.
 *
 */

public interface HttpClientInterface {
    Retrofit getClient();
}
