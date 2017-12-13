package com.hackernews.reader.data;

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

@SuppressWarnings("ALL")
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

    @Override
    public Retrofit getClient() {


        if( retrofit == null){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.addInterceptor(new Interceptor());
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

    private static class Interceptor implements okhttp3.Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {

            URI uri = chain.request().url().uri();

            String responseString = "";
            switch(uri.getPath()){
                case "/v0/topstories":
                    responseString = getTopStoriesId();
                    break;
            }

            Response response = new Response.Builder()
                    .code(200)
                    .message(responseString)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                    .addHeader("content-type", "application/json")
                    .build();

            return response;
        }

        private String getTopStoriesId(){
            StringBuilder strBuilder = new StringBuilder();
            for(Map.Entry<Integer, NewsItem> news : FakeData.newsItems.entrySet()){
                strBuilder.append(news.getKey());
                strBuilder.append(",");
            }

            return "["+strBuilder.toString().substring(0,strBuilder.toString().length()-1)+"]";
        }
    }
}
