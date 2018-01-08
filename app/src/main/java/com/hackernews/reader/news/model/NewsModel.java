package com.hackernews.reader.news.model;

import java.util.ArrayList;

/**
 * Created by HJ Chin on 29/11/2017.
 */

@SuppressWarnings("ALL")
public interface NewsModel{

    interface GetIdsCallback{
        void onResponse(ArrayList<NewsItem> newsItems);
        void onErrorResponse(Throwable throwable);
    }

    interface GetNewsItemCallback{
        void onResponse(NewsItem newsItem);
        void onErrorResponse(Throwable throwable);
    }

    ArrayList<NewsItem> getImmutableList();
    void fill(ArrayList<NewsItem> value);
    void getItems(GetNewsItemCallback callback);
    void cancel();
}
