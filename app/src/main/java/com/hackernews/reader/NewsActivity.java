package com.hackernews.reader;

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
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import com.hackernews.reader.util.AppLog;
import com.hackernews.reader.util.UILoader;


public class NewsActivity extends AppCompatActivity implements
        NewsAdapter.Callback,
        SwipeRefreshLayout.OnRefreshListener{

    private static final String NEWS_ITEM = "newsItem";
    private UILoader UILoader;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private ArrayList<NewsItem> newsItems = new ArrayList<>();
    private View loaderView;
    private SwipeRefreshLayout swipeRefresh;

    @Nullable
    private final CountingIdlingResource idlingResource = new CountingIdlingResource("countingIdleResource");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        loaderView = findViewById(R.id.loading_container);
        recyclerView = (RecyclerView)findViewById(R.id.news);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(this);

        UILoader = new UILoader(this, loaderView, recyclerView);

        if(savedInstanceState == null){
            UILoader.showLoader();
            requestNews();
        }else{
            UILoader.showContent();
            newsItems = savedInstanceState.getParcelableArrayList(NEWS_ITEM);
            setupAdapter();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(NEWS_ITEM,newsItems);
    }

    private void requestNews() {

        idlingResource.increment();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://hacker-news.firebaseio.com/v0/topstories.json";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    populateNewsItem(response);
                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    swipeRefresh.setVisibility(View.GONE);
                    if(swipeRefresh.isRefreshing()){
                        swipeRefresh.setRefreshing(false);
                    }

                    String message = "Error loading news";

                    UILoader.showLoadError(message, new View.OnClickListener(){

                        @Override
                        public void onClick(View view) {
                            swipeRefresh.setVisibility(View.VISIBLE);
                            requestNews();
                        }
                    });

                    idlingResource.decrement();
                }
            });

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonArrayRequest);
    }

    private void populateNewsItem(JSONArray response){
        Log.i(AppLog.LOG_TAG, response.toString());

        newsItems = new ArrayList<>();
        for(int i=0;i<response.length();i++){
            NewsItem newsItem = new NewsItem();
            try {
                newsItem.id = (int)response.get(i);
                newsItems.add(newsItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        setupAdapter();
        idlingResource.decrement();
    }

    private void setupAdapter() {
        newsAdapter = new NewsAdapter(NewsActivity.this,newsItems, this);
        recyclerView.setAdapter(newsAdapter);
    }

    private void updateRecyclerView(NewsItem item, int index){

        if(swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }

        if(loaderView.getVisibility() != View.GONE){
            UILoader.showContent();
        }

        newsItems.set(index,item);
        newsAdapter.notifyItemChanged(index);

        idlingResource.decrement();
    }

    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(this, CommentActivity.class);
        i.putExtra(CommentActivity.COMMENT_LIST,newsItems.get(position).kids);
        startActivity(i);
    }

    @Override
    public void requestNews(final int position) {

        idlingResource.increment();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://hacker-news.firebaseio.com/v0/item/"+newsItems.get(position).id+".json";
        GsonRequest<NewsItem> newsRequest = new GsonRequest<>(Request.Method.GET,
            url,
            NewsItem.class,
            new Response.Listener<NewsItem>() {
                @Override
                public void onResponse(NewsItem response) {
                    updateRecyclerView(response, position);
                }
            },
            new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.wtf(AppLog.LOG_TAG,error.getMessage());

                    swipeRefresh.setVisibility(View.GONE);
                    String message = "Error loading news";
                    UILoader.showLoadError(message, new View.OnClickListener(){

                        @Override
                        public void onClick(View view) {
                            swipeRefresh.setVisibility(View.VISIBLE);
                            requestNews();
                        }
                    });

                    idlingResource.decrement();
                    //it will retry when adapter scrolls.
                }
            });

        newsRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(newsRequest);
    }

    @Override
    public void onRefresh() {
        requestNews();
    }

    @VisibleForTesting
    @NonNull
    public CountingIdlingResource getIdlingResource() {
        return idlingResource;
    }
}
