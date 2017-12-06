package com.hackernews.reader.data.comment;

import android.os.Handler;

import com.android.volley.VolleyError;
import com.hackernews.reader.data.FakeData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by HJ Chin on 2/12/2017.
 */

public class FakeCommentData implements CommentModel{

    private Map<Integer, CommentItem> data = new LinkedHashMap<>();
    private boolean connect = true;

    @Override
    public void disconnect() {
        connect = false;
    }

    @Override
    public void connect() {
        connect = true;
    }

    @Override
    public ArrayList<CommentItem> getImmutableList() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void fill(ArrayList<CommentItem> items) {
        data = new LinkedHashMap<>();
        for(CommentItem i:items){
            data.put(i.id,i);
        }
    }

    @Override
    public void fill(int[] value) {
        data = new LinkedHashMap<>();

        for(int v: value){
            CommentItem item = new CommentItem();
            item.id = v;
            data.put(item.id,item);
        }
    }

    @Override
    public void getItem(final int id, final GetItemCallback callback) {

        if(connect){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    data.put(id, FakeData.commentItems.get(id));
                    callback.onResponse(data.get(id));
                }
            },1000);
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onErrorResponse(new VolleyError());
            }
        },1000);
    }
}