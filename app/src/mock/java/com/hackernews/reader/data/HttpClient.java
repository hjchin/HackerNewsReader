package com.hackernews.reader.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HJ Chin on 28/11/2017.
 */

public class HttpClient implements HttpClientInterface {

    private static com.hackernews.reader.data.HttpClient instance;
    private Retrofit retrofit;

    private HttpClient(){
    }

    public static synchronized HttpClient getInstance() {
        if (instance == null) {
            WebServer.init();
            instance = new HttpClient();
        }
        return instance;
    }

    @Override
    public Retrofit getClient(){


        if(retrofit == null){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.addInterceptor(logging);

            retrofit = new Retrofit.Builder()
                    .baseUrl(WebServer.getMockServerUrl())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClientBuilder.build())
                    .build();
        }

        return retrofit;
    }

    public HackerNewsApi getHackerNewsApi(){
        return getClient().create(HackerNewsApi.class);
    }
}
