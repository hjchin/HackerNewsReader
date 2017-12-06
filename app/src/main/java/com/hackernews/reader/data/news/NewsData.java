package com.hackernews.reader.data.news;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by HJ Chin on 28/11/2017.
 *
 */

public class NewsData implements NewsModel {

    private Map<Integer, NewsItem> data = new LinkedHashMap<>();
    private NewsIDRequest idRequest;
    private NewsItemRequest itemRequest;

    NewsData(NewsIDRequest IdRequest, NewsItemRequest itemRequest){
        this.idRequest = IdRequest;
        this.itemRequest = itemRequest;
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

        idRequest.requestId(new NewsIDRequest.Callback() {

            @Override
            public void onResponse(Map<Integer, NewsItem> newsItem) {
                data = newsItem;
                callback.onResponse(getImmutableList());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorResponse(error);
            }
        });
    }

    public void getItem(int newsId, final GetNewsItemCallback callback){

        itemRequest.requestItem(newsId, new NewsItemRequest.Callback() {

            @Override
            public void onResponse(NewsItem newsItem) {
                data.put(newsItem.id,newsItem);
                callback.onResponse(newsItem);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorResponse(error);
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
