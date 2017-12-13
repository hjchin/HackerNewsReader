package com.hackernews.reader.data;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hackernews.reader.data.news.NewsItem;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HJ Chin on 28/11/2017.
 */

public class HttpClient implements HttpClientInterface {

    private static HttpClient mInstance;
    private Retrofit retrofit;
    private String baseUrl = "https://hacker-news.firebaseio.com/";

    private HttpClient(){
    }

    public static synchronized HttpClient getInstance() {
        if (mInstance == null) {
            mInstance = new HttpClient();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return null;
    }

    @Override
    public Retrofit getClient() {


        if( retrofit == null){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.addInterceptor(logging);

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClientBuilder.build())
                    .build();
        }

        return retrofit;
    }

    public HackerNewsApi getHackerNewsApi(){
        return getClient().create(HackerNewsApi.class);
    }
}
