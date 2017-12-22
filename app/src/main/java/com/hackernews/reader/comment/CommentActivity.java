package com.hackernews.reader.comment;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hackernews.reader.data.comment.CommentItem;
import com.hackernews.reader.R;
import com.hackernews.reader.data.comment.CommentProvider;
import com.hackernews.reader.util.UILoader;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity implements
        CommentAdapter.Callback,
        CommentView {

    public static final String COMMENT_LIST = "comment_list";
    private static final String COMMENT_ITEM = "comment_item";
    private View loaderContainer;
    private UILoader UILoader;
    private CommentAdapter commentAdapter;

    @Nullable
    private static CountingIdlingResource idlingResource = new CountingIdlingResource("commentIdleResource");
    private CommentPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        setTitle(R.string.comment);
        RecyclerView commentContainer = (RecyclerView) findViewById(R.id.comment_list);
        commentContainer.setLayoutManager(new LinearLayoutManager(this));
        loaderContainer = findViewById(R.id.loading_container);
        UILoader = new UILoader(this, loaderContainer, commentContainer);

        presenter = new CommentPresenter(CommentProvider.getInstance(), this);

        commentAdapter = new CommentAdapter(presenter.getData(), this);
        commentContainer.setAdapter(commentAdapter);

        if(savedInstanceState == null){
            int[] commentIdList = getIntent().getIntArrayExtra(COMMENT_LIST);

            if(commentIdList == null || commentIdList.length == 0){
                showEmpty(commentContainer);
                return;
            }

            idlingResource.increment();
            presenter.setCommentIds(commentIdList);

        }else{
            ArrayList<Parcelable> retrieved = savedInstanceState.getParcelableArrayList(COMMENT_ITEM);
            ArrayList<CommentItem> items = new ArrayList<>();
            for(Parcelable p : retrieved){
                items.add((CommentItem)p);
            }

            idlingResource.increment();
            presenter.restoreState(items);
            UILoader.showContent();
        }
    }

    private void showEmpty(RecyclerView commentContainer) {
        commentContainer.setVisibility(View.GONE);
        loaderContainer.setVisibility(View.GONE);
        findViewById(R.id.no_comment).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(COMMENT_ITEM,presenter.getData());
    }

    @Override
    public void requestComment(final int position, CommentItem comment) {
        idlingResource.increment();
        presenter.loadItem(comment.id, position);
    }

    @Override
    public void requestReply(int position) {
        Intent i = new Intent(this, CommentActivity.class);
        i.putExtra(COMMENT_LIST,presenter.getData().get(position).kids);
        startActivity(i);
    }

    @Override
    public void fillAdapter(ArrayList<CommentItem> item) {
        commentAdapter.setData(item);
        idlingResource.decrement();
    }

    @Override
    public void showError(Throwable t) {
        UILoader.showLoadError("Error loading comment"
            ,new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    commentAdapter.notifyDataSetChanged();
                }
            });
        idlingResource.decrement();
    }

    @Override
    public void refreshItem(int position, CommentItem item) {

        if(loaderContainer.getVisibility() != View.GONE){
            UILoader.showContent();
        }

        commentAdapter.setData(position, item);
        idlingResource.decrement();
    }

    @VisibleForTesting
    @NonNull
    public static CountingIdlingResource getIdlingResource() {
        return idlingResource;
    }
}
