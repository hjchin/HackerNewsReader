package com.hackernews.reader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class CommentAdapterActivity extends AppCompatActivity implements CommentAdapter.Callback{

    public CommentAdapter adapter;
    private ArrayList<CommentItem> commentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_adapter);

    }

    public void setCommentItem(ArrayList<CommentItem> commentItem){
        this.commentItem = commentItem;
        adapter = new CommentAdapter(commentItem,this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void requestComment(int position) {
        commentItem.get(position).by = "by"+position;
    }

    @Override
    public void requestReply(int position) {

    }
}
