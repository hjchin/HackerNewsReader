package com.hackernews.reader.data.news;

import android.content.Context;

import com.hackernews.reader.data.HttpClient;

/**
 * Created by HJ Chin on 29/11/2017.
 */

public class NewsProvider {

    private static NewsData newsData;

    public static NewsData getInstance(Context context) {
        if(newsData == null){
            newsData = new NewsData(HttpClient.getInstance().getHackerNewsApi());
        }

        return newsData;
    }
}
