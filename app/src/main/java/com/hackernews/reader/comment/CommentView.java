package com.hackernews.reader.comment;

import com.android.volley.VolleyError;
import com.hackernews.reader.data.comment.CommentItem;

import java.util.ArrayList;

/**
 * Created by HJ Chin on 30/11/2017.
 */

interface CommentView {
    void fillAdapter(ArrayList<CommentItem> item);
    void showError(VolleyError error);
    void refreshItem(int position, CommentItem item);
}
