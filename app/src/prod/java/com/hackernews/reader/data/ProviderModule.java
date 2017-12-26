package com.hackernews.reader.di;

import com.hackernews.reader.data.comment.CommentModel;
import com.hackernews.reader.data.comment.CommentProvider;
import com.hackernews.reader.data.news.NewsModel;
import com.hackernews.reader.data.news.NewsProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by HJ Chin on 23/12/2017.
 */

@Module
public class ProviderModule {

    @Provides
    @Singleton
    public NewsModel provideNewsModel(){
        return NewsProvider.getInstance();
    }

    @Provides
    @Singleton
    public CommentModel provideCommentModel(){
        return CommentProvider.getInstance();
    }
}
