package com.hackernews.reader.data;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

import retrofit2.Retrofit;

import static junit.framework.Assert.assertTrue;

/**
 * Created by HJ Chin on 5/12/2017.
 */

@SuppressWarnings("ALL")
@RunWith(RobolectricTestRunner.class)
public class HttpClientTest {

    @Test
    public void testRestApi(){
        HttpClientActivity activity = Robolectric.setupActivity(HttpClientActivity.class);
        assertTrue(com.hackernews.reader.data.HttpClient.getInstance() != null);
        assertTrue(activity.api.getClient() != null);
        assertTrue(activity.api.getClient() instanceof Retrofit);
    }

    @After
    public void teardown(){

        try {
            WebServer.getInstance().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
