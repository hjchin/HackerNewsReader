package com.propertygurutest.hackernewsreader;

import com.android.volley.VolleyError;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import java.util.concurrent.CountDownLatch;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * Created by HJ Chin on 12/11/2017.
 * this unit test is to test the GsonRequest parseNetworkResponse function,
 * more specifically if GsonRequest could correctly return custom type response.
 */

@RunWith(RobolectricTestRunner.class)
public class GsonRequestTest {

    private final CountDownLatch latch = new CountDownLatch(1);

    @Test
    public void testSuccessfulGsonRequest(){
        GsonRequestActivity activity = Robolectric.setupActivity(GsonRequestActivity.class);
        GsonRequestActivity.ResponseListener callback = mock(GsonRequestActivity.ResponseListener.class);
        ArgumentCaptor<NewsItem> captor = ArgumentCaptor.forClass(NewsItem.class);
        activity.setResponseListener(callback);
        activity.requestNewsItem();
        verify(callback, timeout(50000)).onSuccess(captor.capture());
        NewsItem actual = captor.getValue();
        GsonRequestActivity.NewsItemSample expected = new GsonRequestActivity.NewsItemSample();
        assertEquals(expected.by,actual.by);
    }

    @Test
    public void testExceptionGsonRequest(){
        GsonRequestActivity activity = Robolectric.setupActivity(GsonRequestActivity.class);
        GsonRequestActivity.ResponseListener callback = mock(GsonRequestActivity.ResponseListener.class);
        ArgumentCaptor<VolleyError> captor = ArgumentCaptor.forClass(VolleyError.class);
        activity.setResponseListener(callback);
        activity.requestNewsItemWithException();
        verify(callback, timeout(50000)).onError(captor.capture());
//        VolleyError actual = captor.getValue();
//        GsonRequestActivity.NewsItemSample expected = new GsonRequestActivity.NewsItemSample();
//        assertEquals(expected.by,actual.getCause()getMessage());
    }
}
