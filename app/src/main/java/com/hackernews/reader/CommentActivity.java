package com.hackernews.reader;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hackernews.reader.util.UILoader;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity implements
        CommentAdapter.Callback{

    public static final String COMMENT_LIST = "comment_list";
    private static final String COMMENT_ITEM = "comment_item";
    private View loaderContainer;
    private UILoader UILoader;
    private ArrayList<CommentItem> commentItems;
    private CommentAdapter commentAdapter;

    @Nullable
    private final CountingIdlingResource idlingResource = new CountingIdlingResource("commentIdleResource");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        setTitle(R.string.comment);
        RecyclerView commentContainer = (RecyclerView) findViewById(R.id.comment_list);
        loaderContainer = findViewById(R.id.loading_container);
        UILoader = new UILoader(this, loaderContainer, commentContainer);

        if(savedInstanceState == null){
            int[] commentIdList = getIntent().getIntArrayExtra(COMMENT_LIST);

            if(commentIdList == null || commentIdList.length == 0){
                commentContainer.setVisibility(View.GONE);
                loaderContainer.setVisibility(View.GONE);
                findViewById(R.id.no_comment).setVisibility(View.VISIBLE);
                return;
            }

            commentItems = new ArrayList<>();

            for(int commentId : commentIdList){
                CommentItem commentItem = new CommentItem();
                commentItem.id = commentId;
                commentItems.add(commentItem);
            }
            UILoader.showLoader();
            idlingResource.increment();
        }else{
            UILoader.showContent();
            commentItems = savedInstanceState.getParcelableArrayList(COMMENT_ITEM);
        }

        commentAdapter = new CommentAdapter(commentItems, this);
        commentContainer.setLayoutManager(new LinearLayoutManager(this));
        commentContainer.setAdapter(commentAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(COMMENT_ITEM,commentItems);
    }

    @Override
    public void requestComment(final int position) {

        idlingResource.increment();

        RequestQueue request = Volley.newRequestQueue(this);
        String url = "https://hacker-news.firebaseio.com/v0/item/"+ commentItems.get(position).id+".json";

        GsonRequest<CommentItem> newsRequest = new GsonRequest<>(Request.Method.GET,
                url,
                CommentItem.class,
                new Response.Listener<CommentItem>() {
                    @Override
                    public void onResponse(CommentItem comment) {

                        //skip invalid data
                        if(comment.by != null){
                            updateRecyclerView(comment, position);
                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        idlingResource.decrement();
                        UILoader.showLoadError("Error loading comment"
                            ,new View.OnClickListener(){
                                @Override
                                public void onClick(View view) {

                                    idlingResource.increment();
                                    commentAdapter.notifyDataSetChanged();
                                }
                            });
                    }
                });

        request.add(newsRequest);
    }

    @Override
    public void requestReply(int position) {
        Intent i = new Intent(this, CommentActivity.class);
        i.putExtra(COMMENT_LIST,commentItems.get(position).kids);
        startActivity(i);
    }

    private void updateRecyclerView(CommentItem commentItem, int itemIndex) {

        if(loaderContainer.getVisibility() != View.GONE){
            UILoader.showContent();
            idlingResource.decrement();
        }

        commentItems.set(itemIndex,commentItem);
        commentAdapter.notifyItemChanged(itemIndex);
        idlingResource.decrement();
    }
}
