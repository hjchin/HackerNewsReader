package com.propertygurutest.hackernewsreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class NewsAdapterActivity extends AppCompatActivity implements NewsAdapter.Callback{

    public NewsAdapter adapter;
    private ArrayList<NewsItem> newsItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_adapter);
    }

    public void setNewsItem(ArrayList<NewsItem> newsItem){
        this.newsItem = newsItem;
        adapter = new NewsAdapter(this,newsItem,this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.newsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void requestNews(int position) {
        newsItem.get(position).title = "title"+position;
    }
}
