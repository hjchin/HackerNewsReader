package com.hackernews.reader.data.news;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hackernews.reader.data.RestApiInterface;
import com.hackernews.reader.util.GsonRequest;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by HJ Chin on 28/11/2017.
 *
 */

//public class NewsIDRequest implements Response.Listener<JSONArray>, Response.ErrorListener {
class NewsIDRequest implements Response.Listener<Integer[]>, Response.ErrorListener {
    private RestApiInterface request;

    public interface Callback{
        void onResponse(Map<Integer, NewsItem> newsItem);
        void onErrorResponse(VolleyError error);
    }

    private Callback callback;

    NewsIDRequest(RestApiInterface request){
        this.request = request;
    }

    void requestId(Callback callback){
        this.callback = callback;
        String url = "https://hacker-news.firebaseio.com/v0/topstories.json";
        GsonRequest<Integer[]> newsIdRequest = new GsonRequest<>(Request.Method.GET, url,Integer[].class,this,this);
        request.getRequestQueue().add(newsIdRequest);
    }

    @Override
    public void onResponse(Integer[] response) {

        Map<Integer, NewsItem> newsItems = new LinkedHashMap<>();
        for(Integer rs : response){
            NewsItem newsItem = new NewsItem();
            newsItem.id = rs;
            newsItems.put(newsItem.id, newsItem);
        }

        callback.onResponse(newsItems);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        callback.onErrorResponse(error);
    }
}
