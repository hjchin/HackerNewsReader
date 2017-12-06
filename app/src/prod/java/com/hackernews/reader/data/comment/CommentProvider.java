package com.hackernews.reader.data.comment;

import android.content.Context;

import com.hackernews.reader.data.RestApi;

/**
 * Created by HJ Chin on 30/11/2017.
 */

public class CommentProvider {

    private static CommentData commentData;

    public static CommentData getInstance(Context context) {
        if(commentData == null){
            commentData = new CommentData(new CommentItemRequest(RestApi.getInstance(context)));
        }

        return commentData;
    }
}
