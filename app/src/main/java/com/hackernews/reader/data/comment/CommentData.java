package com.hackernews.reader.data.comment;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by HJ Chin on 30/11/2017.
 */

public class CommentData implements CommentModel {

    private CommentItemRequest dataRequest;

    private Map<Integer, CommentItem> data = new LinkedHashMap<>();

    public CommentData(CommentItemRequest dataRequest){
        this.dataRequest = dataRequest;
    }

    public void fill(ArrayList<CommentItem> items){
        data = new LinkedHashMap<>();
        for(CommentItem i:items){
            data.put(i.id,i);
        }
    }

    public void fill(int[] commentIds){
        data = new LinkedHashMap<>();
        for(int id : commentIds){
            CommentItem item = new CommentItem();
            item.id = id;
            data.put(item.id,item);
        }
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void connect() {

    }

    public ArrayList<CommentItem> getImmutableList() {
        return new ArrayList<>(data.values());
    }

    public void getItem(final int commentId, final GetItemCallback callback) {

        dataRequest.requestItem(commentId, new CommentItemRequest.Callback() {

            @Override
            public void onResponse(CommentItem item) {
                data.put(commentId,item);
                callback.onResponse(item);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorResponse(error);
            }
        });
    }
}
