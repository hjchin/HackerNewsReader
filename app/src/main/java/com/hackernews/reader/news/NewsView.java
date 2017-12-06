package com.hackernews.reader.news;

import com.android.volley.VolleyError;
import com.hackernews.reader.data.news.NewsItem;

import java.util.ArrayList;

/**
 * Created by HJ Chin on 28/11/2017.
 *
 */

interface NewsView {
    void showProgressBar();
    void fillAdapter(ArrayList<NewsItem> item);
    void showError(VolleyError error);
    void refreshItem(int position, NewsItem item);
}
