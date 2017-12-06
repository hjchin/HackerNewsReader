package com.hackernews.reader.data.news;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hackernews.reader.data.RestApiInterface;
import com.hackernews.reader.util.GsonRequest;

/**
 * Created by HJ Chin on 28/11/2017.
 *
 */

class NewsItemRequest{
    public interface Callback{
        void onResponse(NewsItem newsItem);
        void onErrorResponse(VolleyError error);
    }

    private RestApiInterface api;

    NewsItemRequest(RestApiInterface api){
        this.api = api;
    }

    void requestItem(int newsId, final Callback callback){

        String url = "https://hacker-news.firebaseio.com/v0/item/"+newsId+".json";

        final GsonRequest<NewsItem> newsRequest = new GsonRequest<>(Request.Method.GET,
                url,
                NewsItem.class,
                new Response.Listener<NewsItem>() {
                    @Override
                    public void onResponse(NewsItem response) {

                        if(response.title == null){
                            response.title = "";
                        }

                        callback.onResponse(response);
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onErrorResponse(error);
                    }
                });

        newsRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        api.getRequestQueue().add(newsRequest);
    }
}
