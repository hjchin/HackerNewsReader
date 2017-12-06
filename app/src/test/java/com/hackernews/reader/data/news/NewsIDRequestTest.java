package com.hackernews.reader.data.news;

import com.android.volley.VolleyError;
import com.hackernews.reader.data.RestApiInterface;
import com.hackernews.reader.data.news.mock.FakeNewsIDRestApi;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

/**
 * Created by HJ Chin on 6/12/2017.
 */

public class NewsIDRequestTest {

    @Test
    public void testRequestIdOK(){

        RestApiInterface fakeApi = new FakeNewsIDRestApi(false);
        NewsIDRequest request = new NewsIDRequest(fakeApi);

        NewsIDRequest.Callback callback = mock(NewsIDRequest.Callback.class);
        request.requestId(callback);

        ArgumentCaptor<Map<Integer,NewsItem>> argumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(callback,timeout(50000)).onResponse(argumentCaptor.capture());

        assertEquals(5,argumentCaptor.getValue().size());

    }

    @Test
    public void testRequestIdFailed(){

        RestApiInterface fakeApi = new FakeNewsIDRestApi(true);
        NewsIDRequest request = new NewsIDRequest(fakeApi);

        NewsIDRequest.Callback callback = mock(NewsIDRequest.Callback.class);
        request.requestId(callback);

        ArgumentCaptor<VolleyError> argumentCaptor = ArgumentCaptor.forClass(VolleyError.class);
        verify(callback,timeout(50000)).onErrorResponse(argumentCaptor.capture());
    }
}
