package com.hackernews.reader.data;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by HJ Chin on 28/11/2017.
 */

public class RestApi implements RestApiInterface{

    private static RestApi mInstance;
    private Context context;
    private RequestQueue requestQueue;

    private RestApi(Context context){
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized RestApi getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RestApi(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
}
