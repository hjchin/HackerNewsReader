package com.hackernews.reader.comment.view;

import com.hackernews.reader.comment.model.CommentItem;

import java.util.ArrayList;

/**
 * Created by HJ Chin on 30/11/2017.
 */

@SuppressWarnings("ALL")
public interface CommentView {
    void fillAdapter(ArrayList<CommentItem> item);
    void showError(Throwable throwable);
    void addToAdapter(CommentItem item);
}
