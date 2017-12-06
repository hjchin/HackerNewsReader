package com.hackernews.reader.data.comment;

import com.android.volley.VolleyError;
import com.hackernews.reader.data.BaseModel;

import java.util.ArrayList;

/**
 * Created by HJ Chin on 30/11/2017.
 */

public interface CommentModel extends BaseModel {

    interface GetItemCallback{
        void onResponse(CommentItem item);
        void onErrorResponse(VolleyError error);
    }

    ArrayList<CommentItem> getImmutableList();
    void fill(ArrayList<CommentItem> items);
    void fill(int[] commentIds);
    void getItem(int commentId, final GetItemCallback callback);
}
