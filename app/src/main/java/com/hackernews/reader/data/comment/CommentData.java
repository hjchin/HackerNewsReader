package com.hackernews.reader.data.comment;

import com.hackernews.reader.data.HackerNewsApi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HJ Chin on 30/11/2017.
 */

@SuppressWarnings("ALL")
public class CommentData implements CommentModel {

    private HackerNewsApi api;
    private Map<Integer, CommentItem> data = new LinkedHashMap<>();

    CommentData(HackerNewsApi api){
        this.api = api;
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

        api.getCommentItem(commentId).enqueue(new Callback<CommentItem>() {
            @Override
            public void onResponse(Call<CommentItem> call, Response<CommentItem> response) {
                CommentItem item = response.body();
                data.put(commentId,item);
                callback.onResponse(item);
            }

            @Override
            public void onFailure(Call<CommentItem> call, Throwable t) {
                callback.onErrorResponse(t);
            }
        });
    }
}
