package com.hackernews.reader.news;


import com.hackernews.reader.BasePresenter;
import com.hackernews.reader.data.news.NewsModel;
import com.hackernews.reader.data.news.NewsItem;

import java.util.ArrayList;

/**
 * Created by HJ Chin on 28/11/2017.
 *
 */

class NewsPresenter implements BasePresenter{

    private final NewsView view;
    private final NewsModel model;

    NewsPresenter(NewsModel model, NewsView view){
        this.model = model;
        this.view = view;
    }

    ArrayList<NewsItem> getData(){
        return model.getImmutableList();
    }

    void restoreState(ArrayList<NewsItem>  value){
        model.fill(value);
        view.fillAdapter(getData());
    }

    void loadNews(){

        view.showProgressBar();

        model.getIds(new NewsModel.GetIdsCallback() {
            @Override
            public void onResponse(ArrayList<NewsItem> newsItems) {
                view.fillAdapter(newsItems);
            }

            @Override
            public void onErrorResponse(Throwable error) {
                view.showError(error);
            }
        });
    }

    @Override
    public void loadItem(final int newsId, final int position){

        model.getItem(newsId, new NewsModel.GetNewsItemCallback() {
            @Override
            public void onResponse(NewsItem newsItem) {
                view.refreshItem(position, newsItem);
            }

            @Override
            public void onErrorResponse(Throwable throwable) {
                view.showError(throwable);
            }
        });
    }
}
