package com.hackernews.reader.data.comment;

import android.content.Context;

/**
 * Created by HJ Chin on 2/12/2017.
 */

@SuppressWarnings("ALL")
public class CommentProvider {

    private static CommentModel commentData;

    public static CommentModel getInstance(Context context) {
        if(commentData == null){
            commentData = new FakeCommentData();
        }

        return commentData;
    }
}
