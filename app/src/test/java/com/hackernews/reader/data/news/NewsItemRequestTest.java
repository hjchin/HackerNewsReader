package com.hackernews.reader.data.news;

import com.android.volley.VolleyError;
import com.hackernews.reader.data.RestApiInterface;
import com.hackernews.reader.data.news.mock.FakeBrokenNewsItemRestApi;
import com.hackernews.reader.data.news.mock.FakeNewsItemRestApi;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

/**
 * Created by HJ Chin on 6/12/2017.
 */

public class NewsItemRequestTest {

    @Test
    public void testNewsItemRequest(){

        RestApiInterface api = new FakeNewsItemRestApi(false);
        NewsItemRequest itemRequest = new NewsItemRequest(api);

        NewsItemRequest.Callback callback = mock(NewsItemRequest.Callback.class);
        itemRequest.requestItem(0,callback);

        ArgumentCaptor<NewsItem> argumentCaptor = ArgumentCaptor.forClass(NewsItem.class);
        verify(callback, timeout(50000)).onResponse(argumentCaptor.capture());
        assertEquals(0,argumentCaptor.getValue().id);

    }

    @Test
    public void testFailedNewsItemRequest(){

        RestApiInterface api = new FakeNewsItemRestApi(true);
        NewsItemRequest itemRequest = new NewsItemRequest(api);

        NewsItemRequest.Callback callback = mock(NewsItemRequest.Callback.class);
        itemRequest.requestItem(0,callback);

        ArgumentCaptor<VolleyError> argumentCaptor = ArgumentCaptor.forClass(VolleyError.class);
        verify(callback, timeout(50000)).onErrorResponse(argumentCaptor.capture());
    }

    @Test
    public void testBrokenNewsItemRequest(){
        RestApiInterface api = new FakeBrokenNewsItemRestApi(false);
        NewsItemRequest itemRequest = new NewsItemRequest(api);

        NewsItemRequest.Callback callback = mock(NewsItemRequest.Callback.class);
        itemRequest.requestItem(0,callback);

        ArgumentCaptor<NewsItem> argumentCaptor = ArgumentCaptor.forClass(NewsItem.class);
        verify(callback, timeout(50000)).onResponse(argumentCaptor.capture());
        assertEquals("",argumentCaptor.getValue().title);
    }
}
