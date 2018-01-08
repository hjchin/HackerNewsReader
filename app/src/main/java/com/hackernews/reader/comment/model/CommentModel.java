package com.hackernews.reader.comment.model;

import java.util.ArrayList;

/**
 * Created by HJ Chin on 30/11/2017.
 *
 */

public interface CommentModel{

    interface GetItemCallback{
        void onResponse(CommentItem item);
        void onErrorResponse(Throwable throwable);
    }

    ArrayList<CommentItem> getList();
    void fill(ArrayList<CommentItem> items);
    void fill(int[] commentIds);
    void getItems(final GetItemCallback callback);
}
