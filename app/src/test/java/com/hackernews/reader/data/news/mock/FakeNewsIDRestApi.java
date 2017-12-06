package com.hackernews.reader.data.news.mock;

import com.android.volley.AuthFailureError;
import com.android.volley.ExecutorDelivery;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.NoCache;
import com.google.common.collect.Lists;
import com.hackernews.reader.data.RestApiInterface;

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

/**
 * Created by HJ Chin on 6/12/2017.
 */

public class FakeNewsIDRestApi implements RestApiInterface {

    private RequestQueue requestQueue;
    private boolean enableException;

    public FakeNewsIDRestApi(boolean enableException){
        this.enableException = enableException;
        requestQueue = getRequestQueue();
    }

    @Override
    public RequestQueue getRequestQueue() {
        if(requestQueue == null){
            requestQueue = new FakeNewsIDRequestQueue(enableException);
            requestQueue.start();
        }

        return requestQueue;
    }

    private static class FakeNewsIDRequestQueue extends RequestQueue {
        public FakeNewsIDRequestQueue(boolean enableException) {
            super(new NoCache(),
                    new BasicNetwork(new FakeNewsIDHttpStack(enableException)),
                    1,
                    new ExecutorDelivery(Executors.newSingleThreadExecutor()));
        }
    }

    private static class FakeNewsIDHttpStack implements HttpStack {

        private boolean enableException = false;

        FakeNewsIDHttpStack(boolean enableException) {
            this.enableException = enableException;
        }

        @Override
        public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
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
                    new BasicHeader("Content-Length","11"),
                    new BasicHeader("Content-Type","application/json; charset=utf-8"),
                    new BasicHeader("Date", dateFormat.format(new Date())),
                    new BasicHeader("Server","Apache/1.3.42 (Unix) mod_ssl/2.8.31 OpenSSL/0.9.8e"),
                    new BasicHeader("X-Android-Received-Millis","1512551742310"),
                    new BasicHeader("X-Android-Response-Source","NETWORK 200"),
                    new BasicHeader("X-Android-Response-Protocol","http/1.1"),
                    new BasicHeader("X-Android-Sent-Millis","1512551741950")
            );
        }

        private HttpEntity createEntity(Request<?> request) throws UnsupportedEncodingException {

            if(enableException){
                return new StringEntity("hello world");
            }

            return new StringEntity("[0,1,2,3,4]");
        }
    }
}
