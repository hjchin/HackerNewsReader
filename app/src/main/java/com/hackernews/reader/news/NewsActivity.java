package com.hackernews.reader.news;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import com.hackernews.reader.NewsReaderApplication;
import com.hackernews.reader.comment.CommentActivity;
import com.hackernews.reader.data.news.NewsItem;

import com.hackernews.reader.R;
import com.hackernews.reader.data.news.NewsModel;
import com.hackernews.reader.util.UILoader;

import javax.inject.Inject;


public class NewsActivity extends AppCompatActivity implements
        NewsAdapter.Callback,
        SwipeRefreshLayout.OnRefreshListener,
        NewsView {

    private static final String NEWS_ITEM = "newsItem";
    private UILoader UILoader;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private NewsPresenter presenter;

    @Inject
    public NewsModel newsModel;

    @Nullable
    private static CountingIdlingResource idlingResource = new CountingIdlingResource("countingIdleResource");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((NewsReaderApplication)getApplication()).getAppComponent().inject(this);

        setContentView(R.layout.activity_news);

        View loaderView = findViewById(R.id.loading_container);
        recyclerView = (RecyclerView)findViewById(R.id.news);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(this);

        UILoader = new UILoader(this, loaderView, recyclerView);
        UILoader.showLoader();

        presenter = new NewsPresenter(newsModel,this, idlingResource);

        if(savedInstanceState == null){
            presenter.loadNews();
        }else{
            ArrayList<NewsItem> newsItems = savedInstanceState.getParcelableArrayList(NEWS_ITEM);
            presenter.restoreState(newsItems);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(NEWS_ITEM, presenter.getData());
    }

    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(this, CommentActivity.class);
        i.putExtra(CommentActivity.COMMENT_LIST,presenter.getData().get(position).kids);
        startActivity(i);
    }

    @Override
    public void onRefresh() {
        newsAdapter.clear();
        newsAdapter = null;
        presenter.refreshNews();
    }

    @Override
    public void showProgressBar() {
        UILoader.showLoader();
    }

    @Override
    public void addToAdapter(NewsItem item) {
        if(swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }

        if(newsAdapter == null){
            ArrayList<NewsItem> newsItem = new ArrayList<>();
            newsItem.add(item);
            newsAdapter = new NewsAdapter(newsItem, this);
            recyclerView.setAdapter(newsAdapter);
            UILoader.showContent();
        }else{
            newsAdapter.addItem(item);
        }
    }

    @Override
    public void showError(Throwable throwable) {
        swipeRefresh.setVisibility(View.GONE);

        if(swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }

        String message = "Error loading news";
        UILoader.showLoadError(message, new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                swipeRefresh.setVisibility(View.VISIBLE);
                presenter.loadNews();
            }
        });
    }

    @VisibleForTesting
    @NonNull
    public CountingIdlingResource getIdlingResource() {
        return idlingResource;
    }
}
