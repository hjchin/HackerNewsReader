package com.hackernews.reader.data.news;

import com.hackernews.reader.data.HackerNewsApi;
import com.hackernews.reader.news.model.NewsData;
import com.hackernews.reader.news.model.NewsItem;

import org.junit.Test;

import java.util.ArrayList;


import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by HJ Chin on 5/12/2017.
 */

@SuppressWarnings("ALL")
public class NewsDataTest {

    @Test
    public void testFill(){
        HackerNewsApi hackerNewsApi = mock(HackerNewsApi.class);
        NewsData newsData = new NewsData(hackerNewsApi);
        ArrayList<NewsItem> items = new ArrayList<>();
        NewsItem item = new NewsItem();
        item.id = 1;
        items.add(item);
        newsData.fill(items);
        assertEquals(item.id,newsData.getImmutableList().get(0).id);
    }

}
