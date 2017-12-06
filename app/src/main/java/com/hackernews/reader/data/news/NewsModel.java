package com.hackernews.reader.data.news;

import com.android.volley.VolleyError;
import com.hackernews.reader.data.BaseModel;

import java.util.ArrayList;

/**
 * Created by HJ Chin on 29/11/2017.
 */

public interface NewsModel extends BaseModel {

    interface GetIdsCallback{
        void onResponse(ArrayList<NewsItem> newsItems);
        void onErrorResponse(VolleyError error);
    }

    interface GetNewsItemCallback{
        void onResponse(NewsItem newsItem);
        void onErrorResponse(VolleyError error);
    }

    ArrayList<NewsItem> getImmutableList();
    void fill(ArrayList<NewsItem> value);
    void getIds(GetIdsCallback callback);
    void getItem(int newsId, GetNewsItemCallback callback);
}
