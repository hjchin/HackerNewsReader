package com.hackernews.reader.data.comment.mock;

import com.android.volley.AuthFailureError;
import com.android.volley.ExecutorDelivery;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.NoCache;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.hackernews.reader.data.RestApiInterface;
import com.hackernews.reader.data.comment.CommentItem;

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
 * Created by HJ Chin on 5/12/2017.
 */

public class FakeCommentRestApi implements RestApiInterface {


    private RequestQueue requestQueue;
    private boolean enableException;


    public FakeCommentRestApi(boolean enableException){
        this.enableException = enableException;
        requestQueue = getRequestQueue();
    }


    @Override
    public RequestQueue getRequestQueue() {

        if(requestQueue == null){
            requestQueue = new FakeCommentRequestQueue(enableException);
            requestQueue.start();
        }

        return requestQueue;
    }


    private static class FakeCommentRequestQueue extends RequestQueue {

        FakeCommentRequestQueue(boolean throwException) {
            super(new NoCache(),
                    new BasicNetwork(new FakeCommentHttpStack(throwException)),
                    1,
                    new ExecutorDelivery(Executors.newSingleThreadExecutor()));
        }
    }

    private static class FakeCommentHttpStack implements HttpStack {

        private static final int SIMULATED_DELAY_MS = 5000;
        private boolean enableException = false;


        FakeCommentHttpStack(boolean enableException) {
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
                    new BasicHeader("Date", dateFormat.format(new Date())),
                    new BasicHeader("Server",
                        /* Data below is header info of my server */
                            "Apache/1.3.42 (Unix) mod_ssl/2.8.31 OpenSSL/0.9.8e")
            );
        }

        private HttpEntity createEntity(Request<?> request) throws UnsupportedEncodingException {

            if(enableException){
                return new StringEntity("hello world");
            }

            CommentItem item = new CommentItem();
            item.id = 0;
            item.by = "by0";
            item.kids = new int[]{0};
            item.parent = -1;
            item.text =  "this is comment text of id 0";
            item.time = new Date().getTime()/1000;
            item.type = "comment";

            String JsonString = new Gson().toJson(item);
            return new StringEntity(JsonString);
        }
    }
}
