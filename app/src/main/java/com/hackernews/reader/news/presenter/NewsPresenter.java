package com.hackernews.reader.news.presenter;


import android.support.test.espresso.idling.CountingIdlingResource;
import android.util.Log;

import com.hackernews.reader.news.model.NewsModel;
import com.hackernews.reader.news.model.NewsItem;
import com.hackernews.reader.news.view.NewsView;

import java.util.ArrayList;

/**
 * Created by HJ Chin on 28/11/2017.
 *
 */

public class NewsPresenter{

    private final NewsView view;
    private final NewsModel model;
    private final CountingIdlingResource idlingResource;
    private boolean toDecrease = false;

    public NewsPresenter(NewsModel model, NewsView view, CountingIdlingResource idlingResource){
        this.model = model;
        this.view = view;
        this.idlingResource = idlingResource;
    }

    public ArrayList<NewsItem> getData(){
        return model.getImmutableList();
    }

    public void restoreState(ArrayList<NewsItem>  value){

        idlingResource.increment();
        toDecrease = true;

        model.fill(value);

        ArrayList<NewsItem> list = model.getImmutableList();
        for(NewsItem item : list){
            view.addToAdapter(item);
            if(toDecrease){
                idlingResource.decrement();
                toDecrease = false;
            }
        }
    }

    public void loadNews(){

        view.showProgressBar();

        idlingResource.increment();
        toDecrease = true;

        model.getItems(new NewsModel.GetNewsItemCallback() {
            @Override
            public void onResponse(NewsItem newsItem) {
                Log.i("news item","news item :"+String.valueOf(newsItem.id));
                view.addToAdapter(newsItem);
                if(toDecrease){
                    idlingResource.decrement();
                    toDecrease = false;
                }
            }

            @Override
            public void onErrorResponse(Throwable throwable) {
                Log.i("news item","news item : error");
                view.showError(throwable);
                if(toDecrease){
                    idlingResource.decrement();
                    toDecrease = false;
                }
            }
        });
    }

    public void refreshNews(){
        model.cancel();
        loadNews();
    }
}
