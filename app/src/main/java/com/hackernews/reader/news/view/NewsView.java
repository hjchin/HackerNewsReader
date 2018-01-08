package com.hackernews.reader.news.view;

import com.hackernews.reader.news.model.NewsItem;

/**
 * Created by HJ Chin on 28/11/2017.
 *
 */

public interface NewsView {
    void showProgressBar();
    void addToAdapter(NewsItem item);
    void showError(Throwable throwable);
}
