package com.hackernews.reader.data.comment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hackernews.reader.data.RestApiInterface;
import com.hackernews.reader.util.GsonRequest;

/**
 * Created by HJ Chin on 30/11/2017.
 *
 */

class CommentItemRequest{

    public interface Callback{
        void onResponse(CommentItem item);
        void onErrorResponse(VolleyError error);
    }

    private RestApiInterface api;

    CommentItemRequest(RestApiInterface api){
        this.api = api;
    }

    void requestItem(int commentId, final Callback callback){
        Callback callback1 = callback;

        String url = "https://hacker-news.firebaseio.com/v0/item/"+ commentId+".json";
        GsonRequest<CommentItem> commentItemRequest = new GsonRequest<>(Request.Method.GET,
                url,
                CommentItem.class,
                new Response.Listener<CommentItem>() {
                    @Override
                    public void onResponse(CommentItem response) {
                        if (response.by == null) {
                            response.by = "";
                        }
                        callback.onResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorResponse(error);
            }
        });

        commentItemRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        api.getRequestQueue().add(commentItemRequest);
    }
}
