package com.hackernews.reader.data.news;

import com.hackernews.reader.data.HackerNewsApi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HJ Chin on 28/11/2017.
 *
 */

public class NewsData implements NewsModel {

    private Map<Integer, NewsItem> data = new LinkedHashMap<>();
    private HackerNewsApi api;

    NewsData(HackerNewsApi api){
        this.api = api;
    }

    public ArrayList<NewsItem> getImmutableList(){
        return new ArrayList<>(data.values());
    }

    public void fill(ArrayList<NewsItem> value){
        data = new LinkedHashMap<>();
        for(NewsItem i : value){
            data.put(i.id,i);
        }
    }

    public void getIds(final GetIdsCallback callback){

        Call<Integer[]> call = api.getTopStoriesId();
        call.enqueue(new Callback<Integer[]>() {
            @Override
            public void onResponse(Call<Integer[]> call, Response<Integer[]> response) {

                data = new LinkedHashMap<>();
                for (Integer i : response.body()){
                    NewsItem newsItem = new NewsItem();
                    newsItem.id = i;
                    data.put(i, newsItem);
                }

                callback.onResponse(getImmutableList());
            }

            @Override
            public void onFailure(Call<Integer[]> call, Throwable t) {
                callback.onErrorResponse(t);
            }
        });

    }

    public void getItem(int newsId, final GetNewsItemCallback callback){

        api.getNews(newsId).enqueue(new Callback<NewsItem>() {
            @Override
            public void onResponse(Call<NewsItem> call, Response<NewsItem> response) {
                NewsItem newsItem = response.body();
                data.put(newsItem.id,newsItem);
                callback.onResponse(newsItem);
            }

            @Override
            public void onFailure(Call<NewsItem> call, Throwable t) {
                callback.onErrorResponse(t);
            }
        });
    }

    @Override
    public void disconnect() {
        //do nothing
    }

    @Override
    public void connect() {
        //do nothing
    }
}
