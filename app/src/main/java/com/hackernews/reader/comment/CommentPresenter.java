package com.hackernews.reader.comment;

import com.hackernews.reader.BasePresenter;
import com.hackernews.reader.data.comment.CommentItem;
import com.hackernews.reader.data.comment.CommentModel;

import java.util.ArrayList;

/**
 * Created by HJ Chin on 30/11/2017.
 */

@SuppressWarnings("ALL")
public class CommentPresenter implements BasePresenter{

    private CommentModel data;
    private CommentView view;

    public CommentPresenter(CommentModel model, CommentView view){
        this.data = model;
        this.view = view;
    }

    public void setCommentIds(int[] commentIds){
        data.fill(commentIds);
        view.fillAdapter(getData());
    }

    public void restoreState(ArrayList<CommentItem> item){
        data.fill(item);
        view.fillAdapter(item);
    }

    public ArrayList<CommentItem> getData(){
        return data.getImmutableList();
    }

    @Override
    public void loadItem(int commentId, final int position) {
        data.getItem(commentId, new CommentModel.GetItemCallback(){

            @Override
            public void onResponse(CommentItem item) {
                view.refreshItem(position,item);
            }

            @Override
            public void onErrorResponse(Throwable t) {
                view.showError(t);
            }
        });
    }
}
