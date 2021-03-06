package com.hackernews.reader.comment.presenter;

import android.support.test.espresso.idling.CountingIdlingResource;

import com.hackernews.reader.comment.view.CommentView;
import com.hackernews.reader.comment.model.CommentItem;
import com.hackernews.reader.comment.model.CommentModel;

import java.util.ArrayList;

/**
 * Created by HJ Chin on 30/11/2017.
 */

@SuppressWarnings("ALL")
public class CommentPresenter{

    private final CommentModel data;
    private final CommentView view;
    private final CountingIdlingResource countingIdlingResource;
    private boolean toDecrease = false;

    public CommentPresenter(CommentModel model, CommentView view, CountingIdlingResource countingIdlingResource){
        this.data = model;
        this.view = view;
        this.countingIdlingResource = countingIdlingResource;
    }

    public void restoreState(ArrayList<CommentItem> item){
        countingIdlingResource.increment();
        data.fill(item);
        view.fillAdapter(item);
        countingIdlingResource.decrement();
    }

    public ArrayList<CommentItem> getData(){
        return data.getList();
    }

    public void loadComments(int[] commentIds){
        data.fill(commentIds);
        loadComments();
    }

    public void loadComments(){

        countingIdlingResource.increment();
        toDecrease = true;
        data.getItems(new CommentModel.GetItemCallback(){

            @Override
            public void onResponse(CommentItem item) {
                view.addToAdapter(item);

                if(toDecrease){
                    countingIdlingResource.decrement();
                    toDecrease = false;
                }
            }

            @Override
            public void onErrorResponse(Throwable t) {
                view.showError(t);

                if(toDecrease){
                    countingIdlingResource.decrement();
                    toDecrease = false;
                }
            }
        });
    }
}
