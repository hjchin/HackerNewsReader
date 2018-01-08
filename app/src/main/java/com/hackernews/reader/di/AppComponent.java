package com.hackernews.reader.di;

import com.hackernews.reader.comment.view.CommentActivity;
import com.hackernews.reader.news.view.NewsActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by HJ Chin on 23/12/2017.
 *
 */
@Singleton
@Component(modules={ProviderModule.class,HttpClientModule.class})
public interface AppComponent{
    void inject(NewsActivity activity);
    void inject(CommentActivity activity);
}
