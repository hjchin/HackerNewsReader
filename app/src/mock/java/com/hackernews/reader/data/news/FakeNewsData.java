package com.hackernews.reader.data.news;

import android.os.Handler;
import android.os.Looper;

import com.android.volley.VolleyError;
import com.hackernews.reader.data.FakeData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by HJ Chin on 29/11/2017.
 *
 */

public class FakeNewsData implements NewsModel {

    Map<Integer, NewsItem> data;
    private boolean connect = true;

    @Override
    public ArrayList<NewsItem> getImmutableList() {
        if(data == null) data = new LinkedHashMap<>();
        return new ArrayList<>(data.values());
    }

    @Override
    public void fill(ArrayList<NewsItem> value) {
        data = new LinkedHashMap<>();
        for(NewsItem item : value){
            data.put(item.id,item);
        }
    }

    @Override
    public void getIds(final GetIdsCallback callback) {

        if(connect){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    data = new LinkedHashMap<>();
                    for(int i = 0; i< FakeData.newsItems.size(); i++){
                        NewsItem item = new NewsItem();
                        item.id = FakeData.newsItems.get(i).id;
                        data.put(item.id,item);
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    callback.onResponse(new ArrayList<NewsItem>(data.values()));
                }
            });

            return;
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                VolleyError error = new VolleyError();
                callback.onErrorResponse(error);
            }
        });
    }

    @Override
    public void getItem(final int newsId, final GetNewsItemCallback callback) {

        if(connect){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    NewsItem item = data.get(newsId);
                    NewsItem fake = FakeData.newsItems.get(newsId);

                    item.by = fake.by;
                    item.descendants = fake.descendants;
                    item.kids = fake.kids;
                    item.score = fake.score;
                    item.time = fake.time;
                    item.title = fake.title;
                    item.type = fake.type;
                    item.url = fake.url;

                    data.put(newsId,item);
                    callback.onResponse(item);
                }
            });
            return;
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                VolleyError error = new VolleyError();
                callback.onErrorResponse(error);
            }
        });

    }

    @Override
    public void disconnect() {
        connect = false;
    }

    @Override
    public void connect() {
        connect = true;
    }
}
