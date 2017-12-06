package com.hackernews.reader.data;

import com.android.volley.RequestQueue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertTrue;

/**
 * Created by HJ Chin on 5/12/2017.
 */

@RunWith(RobolectricTestRunner.class)
public class RestApiTest {


    @Test
    public void testRestApi(){
        RestApiActivity activity = Robolectric.setupActivity(RestApiActivity.class);
        assertTrue(RestApi.getInstance(activity.getApplicationContext()) != null);
        assertTrue(activity.api.getRequestQueue() != null);
        assertTrue(activity.api.getRequestQueue() instanceof RequestQueue);
    }
}
