package com.hackernews.reader.data;

import com.hackernews.reader.data.comment.CommentItem;
import com.hackernews.reader.data.news.NewsItem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by HJ Chin on 13/12/2017.
 */

@SuppressWarnings("ALL")
public interface HackerNewsApi {

    @GET("/v0/topstories.json")
    Call<Integer[]> getTopStoriesId();

    @GET("/v0/item/{newsId}.json")
    Call<NewsItem> getNews(@Path("newsId") int newsId);

    @GET("/v0/item/{commentId}.json")
    Call<CommentItem> getCommentItem(@Path("commentId") int commentId);
}
