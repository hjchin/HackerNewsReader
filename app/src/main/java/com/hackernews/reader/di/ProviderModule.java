package com.hackernews.reader.di;

import com.hackernews.reader.data.HttpClient;
import com.hackernews.reader.comment.model.CommentData;
import com.hackernews.reader.comment.model.CommentModel;
import com.hackernews.reader.news.model.NewsData;
import com.hackernews.reader.news.model.NewsModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by HJ Chin on 23/12/2017.
 *
 */

@Module
public class ProviderModule {

    @Provides
    @Singleton
    public NewsModel provideNewsModel(HttpClient client){
        return new NewsData(client.getHackerNewsApi());
    }

    @Provides
    public CommentModel provideCommentModel(HttpClient client){
        return new CommentData(client.getHackerNewsApi());
    }
}
