package com.hackernews.reader.data.comment;

import com.hackernews.reader.data.HttpClient;

/**
 * Created by HJ Chin on 30/11/2017.
 */

public class CommentProvider {

    private static CommentData commentData;

    public static CommentData getInstance() {
        if(commentData == null){
            commentData = new CommentData(HttpClient.getInstance().getHackerNewsApi());
        }

        return commentData;
    }
}
