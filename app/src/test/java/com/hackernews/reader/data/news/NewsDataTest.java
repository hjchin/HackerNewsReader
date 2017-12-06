package com.hackernews.reader.data.news;

import com.android.volley.VolleyError;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by HJ Chin on 5/12/2017.
 */

public class NewsDataTest {

    @Test
    public void testFill(){
        NewsIDRequest idRequest = mock(NewsIDRequest.class);
        NewsItemRequest itemRequest = mock(NewsItemRequest.class);
        NewsData newsData = new NewsData(idRequest,itemRequest);
        ArrayList<NewsItem> items = new ArrayList<>();
        NewsItem item = new NewsItem();
        item.id = 1;
        items.add(item);
        newsData.fill(items);
        assertEquals(item.id,newsData.getImmutableList().get(0).id);
    }

    @Test
    public void testGetIdsOK(){
        NewsIDRequest idRequest = mock(NewsIDRequest.class);
        NewsItemRequest itemRequest = mock(NewsItemRequest.class);
        NewsData newsData = new NewsData(idRequest,itemRequest);

        doAnswer(
            new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    Map<Integer, NewsItem> newsItems = new LinkedHashMap<>();
                    for(int i=0;i<5;i++){
                        NewsItem item = new NewsItem();
                        item.id = i;
                        newsItems.put(item.id,item);
                    }
                    ((NewsIDRequest.Callback)invocation.getArgument(0)).onResponse(newsItems);
                    return null;
                }
            }
        ).when(idRequest).requestId(any(NewsIDRequest.Callback.class));

        NewsModel.GetIdsCallback callback = mock(NewsModel.GetIdsCallback.class);
        newsData.getIds(callback);

        ArgumentCaptor<ArrayList<NewsItem>> argumentCaptor = ArgumentCaptor.forClass(ArrayList.class);
        verify(callback).onResponse(argumentCaptor.capture());


        ArrayList<NewsItem> newsItem = argumentCaptor.getValue();
        assertEquals(newsItem.size(), 5);
    }

    @Test
    public void testGetIdsFailed(){
        NewsIDRequest idRequest = mock(NewsIDRequest.class);
        NewsItemRequest itemRequest = mock(NewsItemRequest.class);
        NewsData newsData = new NewsData(idRequest,itemRequest);

        doAnswer(
                new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        ((NewsIDRequest.Callback)invocation.getArgument(0)).onErrorResponse(new VolleyError());
                        return null;
                    }
                }
        ).when(idRequest).requestId(any(NewsIDRequest.Callback.class));

        NewsModel.GetIdsCallback callback = mock(NewsModel.GetIdsCallback.class);
        newsData.getIds(callback);

        ArgumentCaptor<VolleyError> argumentCaptor = ArgumentCaptor.forClass(VolleyError.class);
        verify(callback).onErrorResponse(argumentCaptor.capture());
    }

    @Test
    public void testGetItemsOK(){
        NewsIDRequest idRequest = mock(NewsIDRequest.class);
        NewsItemRequest itemRequest = mock(NewsItemRequest.class);
        NewsData newsData = new NewsData(idRequest,itemRequest);

        doAnswer(
                new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        int id = (int)invocation.getArgument(0);
                        NewsItem newsItem = new NewsItem();
                        newsItem.id = id;
                        ((NewsItemRequest.Callback)invocation.getArgument(1)).onResponse(newsItem);
                        return null;
                    }
                }
        ).when(itemRequest).requestItem(anyInt(),any(NewsItemRequest.Callback.class));

        NewsModel.GetNewsItemCallback callback = mock(NewsModel.GetNewsItemCallback.class);
        newsData.getItem(0,callback);

        ArgumentCaptor<NewsItem> argumentCaptor = ArgumentCaptor.forClass(NewsItem.class);
        verify(callback).onResponse(argumentCaptor.capture());

        assertEquals(0,argumentCaptor.getValue().id);
    }

    @Test
    public void testGetItemsFailed(){
        NewsIDRequest idRequest = mock(NewsIDRequest.class);
        NewsItemRequest itemRequest = mock(NewsItemRequest.class);
        NewsData newsData = new NewsData(idRequest,itemRequest);

        doAnswer(
                new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        int index = (int)invocation.getArgument(0);
                        ((NewsItemRequest.Callback)invocation.getArgument(1)).onErrorResponse(new VolleyError());
                        return null;
                    }
                }
        ).when(itemRequest).requestItem(anyInt(),any(NewsItemRequest.Callback.class));

        NewsModel.GetNewsItemCallback callback = mock(NewsModel.GetNewsItemCallback.class);
        newsData.getItem(0,callback);

        ArgumentCaptor<VolleyError> argumentCaptor = ArgumentCaptor.forClass(VolleyError.class);
        verify(callback).onErrorResponse(argumentCaptor.capture());
    }
}
