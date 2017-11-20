package com.propertygurutest.hackernewsreader;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.ExecutorDelivery;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.NoCache;
import com.android.volley.toolbox.Volley;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.propertygurutest.hackernewsreader.util.AppLog;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class GsonRequestActivity extends AppCompatActivity {

    public static class FakeHttpStack implements HttpStack {

        private static final int SIMULATED_DELAY_MS = 500;
        private final Context context;
        private boolean throwException;

        FakeHttpStack(Context context) {
            this.context = context;
        }

        FakeHttpStack(Context context, boolean throwException) {
            this.context = context;
            this.throwException = throwException;
        }

        @Override
        public HttpResponse performRequest(Request<?> request, Map<String, String> stringStringMap)
                throws IOException, AuthFailureError {
            try {
                Thread.sleep(SIMULATED_DELAY_MS);
            } catch (InterruptedException e) { //Ignored
            }
            HttpResponse response
                    = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 200, "OK"));
            List<Header> headers = defaultHeaders();
            response.setHeaders(headers.toArray(new Header[0]));
            response.setEntity(createEntity(request));
            return response;
        }

        private List<Header> defaultHeaders() {
            DateFormat dateFormat = new SimpleDateFormat("EEE, dd mmm yyyy HH:mm:ss zzz");
            return Lists.<Header>newArrayList(
                    new BasicHeader("Date", dateFormat.format(new Date())),
                    new BasicHeader("Server",
                        /* Data below is header info of my server */
                            "Apache/1.3.42 (Unix) mod_ssl/2.8.31 OpenSSL/0.9.8e")
            );
        }

        /**
         * returns the fake content
         */
        private HttpEntity createEntity(Request request) throws UnsupportedEncodingException {

            if(throwException){
                return new StringEntity("hello world");
            }

            NewsItemSample sample = new NewsItemSample();
            String JsonString = new Gson().toJson(sample);
            return new StringEntity(JsonString);
        }
    }

    static class NewsItemSample extends NewsItem{
        NewsItemSample(){
            by = "author";
            descendants = 101;
            id = 123;
            kids = new int[]{1234,1235};
            score = 100;
            time = 1510531519;
            title = "news title";
            type = "story";
            url =  "https://techcrunch.com/2017/11/12/uber-confirms-softbank-has-agreed-to-invest-billions-in-uber/";
        }
    }

    public static class FakeRequestQueue extends RequestQueue {
        FakeRequestQueue(Context context) {
            super(new NoCache(),
                    new BasicNetwork(new FakeHttpStack(context)),
                    4,
                    new ExecutorDelivery(Executors.newSingleThreadExecutor()));
        }

        FakeRequestQueue(Context context, boolean throwException){
            super(new NoCache(),
                    new BasicNetwork(new FakeHttpStack(context, true)),
                    4,
                    new ExecutorDelivery(Executors.newSingleThreadExecutor()));
        }
    }

    interface ResponseListener{
        void onSuccess(NewsItem item);
        void onError(VolleyError error);
    }

    private ResponseListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setResponseListener(ResponseListener listener){
        this.listener = listener;
    }

    public void requestNewsItem(){

        RequestQueue requestQueue = new FakeRequestQueue(this);
        requestQueue.start();

        GsonRequest<NewsItem> request = new GsonRequest<>(Request.Method.GET, "https://hacker-news.firebaseio.com/v0/item/15683801.json",
                NewsItem.class, new Response.Listener<NewsItem>() {
            @Override
            public void onResponse(NewsItem response) {
                listener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i(AppLog.LOG_TAG,error.toString());
                listener.onError(error);
            }
        });

        requestQueue.add(request);
    }

    public void requestNewsItemWithException(){

        RequestQueue requestQueue = new FakeRequestQueue(this, true);
        requestQueue.start();

        GsonRequest<NewsItem> request = new GsonRequest<>(Request.Method.GET, "https://hacker-news.firebaseio.com/v0/item/15683801.json",
                NewsItem.class, new Response.Listener<NewsItem>() {
            @Override
            public void onResponse(NewsItem response) {
                listener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
            }
        });

        requestQueue.add(request);
    }

}
