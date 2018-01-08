package com.hackernews.reader.data;

import android.util.Log;

import com.google.gson.Gson;
import com.hackernews.reader.comment.model.CommentItem;
import com.hackernews.reader.news.model.NewsItem;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static com.hackernews.reader.data.FakeData.items;


/**
 * Created by HJ Chin on 6/1/2018.
 *
 */

public class WebServer {

    private static MockWebServer webServer;
    private static String mockServerUrl;
    private static boolean returnError = false;

    private static Dispatcher dispatcher = new Dispatcher() {

        @Override
        public MockResponse dispatch(RecordedRequest request) throws InterruptedException {

            if(returnError){
                return new MockResponse().setResponseCode(404);
            }

            if(request.getPath().equals("/v0/topstories.json")) {
                return createStoryIdList();
            }else{

                Pattern pattern = Pattern.compile("/v0/item/\\d+.json");
                Matcher matcher = pattern.matcher(request.getPath());

                if(!matcher.find()){
                    return new MockResponse().setResponseCode(404);
                }

                try{
                    String itemId = request.getPath().replace("/v0/item/","").replace(".json","");
                    return createItem(Integer.parseInt(itemId));
                }catch (Exception e){
                    return new MockResponse().setResponseCode(404);
                }
            }
        }
    };

    public static MockWebServer getInstance(){
        if(webServer == null){

            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        webServer = new MockWebServer();
                        webServer.setDispatcher(dispatcher);
                        webServer.start();
                        mockServerUrl = webServer.url("/").toString();
                        System.out.println("Server URL:"+mockServerUrl);
                        Log.i("Server URL",mockServerUrl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            while(mockServerUrl == null){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return webServer;
    }

    public static MockWebServer init(){
        returnError = false;
        return getInstance();
    }

    public static String getMockServerUrl(){
        return mockServerUrl;
    }

    public static void setReturnError(boolean value){
        returnError = value;
    }

    private static MockResponse createStoryIdList() {

        StringBuilder list = new StringBuilder();
        for(Map.Entry<Integer,Item> item : items.entrySet()){
            if(item.getValue() instanceof NewsItem){
                list.append(item.getKey()).append(",");
            }
        }

        list = new StringBuilder("[" + list.substring(0, list.length() - 1) + "]");

        return new MockResponse().setResponseCode(200).setBody(list.toString());
    }

    private static MockResponse createItem(int itemId) {

        Item item = items.get(itemId);
        String itemString = "";

        if(item instanceof NewsItem){
            itemString = new Gson().toJson(item,NewsItem.class);
        }

        if(item instanceof CommentItem){
            itemString = new Gson().toJson(item,CommentItem.class);
        }

        return new MockResponse().setResponseCode(200).setBody(itemString);
    }
}
