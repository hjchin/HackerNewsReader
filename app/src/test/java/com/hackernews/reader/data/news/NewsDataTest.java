package com.hackernews.reader.data.news;

import com.hackernews.reader.data.HackerNewsApi;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by HJ Chin on 5/12/2017.
 */

@SuppressWarnings("ALL")
public class NewsDataTest {

    @Test
    public void testFill(){
        HackerNewsApi hackerNewsApi = mock(HackerNewsApi.class);
        NewsData newsData = new NewsData(hackerNewsApi);
        ArrayList<NewsItem> items = new ArrayList<>();
        NewsItem item = new NewsItem();
        item.id = 1;
        items.add(item);
        newsData.fill(items);
        assertEquals(item.id,newsData.getImmutableList().get(0).id);
    }

    @Test
    public void testGetIdsOK(){
        HackerNewsApi hackerNewsApi = mock(HackerNewsApi.class);
        NewsData newsData = new NewsData(hackerNewsApi);

        when(hackerNewsApi.getTopStoriesId()).thenAnswer(new Answer<Call<Integer[]>>() {
            @Override
            public Call<Integer[]> answer(InvocationOnMock invocation) throws Throwable {

                Call<Integer[]> call = mock(Call.class);

                doAnswer(
                        new Answer() {
                            @Override
                            public Object answer(InvocationOnMock invocation) throws Throwable {
                                Integer[] integers = new Integer[]{1,2,3,4,5};
                                Response<Integer[]> response = Response.success(integers);
                                ((Callback)invocation.getArgument(0)).onResponse(null,response);
                                return null;
                            }
                        }
                ).when(call).enqueue(any(Callback.class));

                return call;
            }
        });

        NewsModel.GetIdsCallback callback = mock(NewsModel.GetIdsCallback.class);
        newsData.getIds(callback);

        ArgumentCaptor<ArrayList<NewsItem>> argumentCaptor = ArgumentCaptor.forClass(ArrayList.class);
        verify(callback).onResponse(argumentCaptor.capture());

        ArrayList<NewsItem> newsItem = argumentCaptor.getValue();
        int counter = 1;
        for(NewsItem item : newsItem){
            assertEquals(counter,item.id);
            counter++;
        }
    }

    @Test
    public void testGetIdsFailed(){
        HackerNewsApi hackerNewsApi = mock(HackerNewsApi.class);
        NewsData newsData = new NewsData(hackerNewsApi);

        when(hackerNewsApi.getTopStoriesId()).thenAnswer(new Answer<Call<Integer[]>>() {
            @Override
            public Call<Integer[]> answer(InvocationOnMock invocation) throws Throwable {

                Call<Integer[]> call = mock(Call.class);

                doAnswer(
                        new Answer() {
                            @Override
                            public Object answer(InvocationOnMock invocation) throws Throwable {
                                Throwable throwable = new Throwable("error");
                                ((Callback)invocation.getArgument(0)).onFailure(null,throwable);
                                return null;
                            }
                        }
                ).when(call).enqueue(any(Callback.class));

                return call;
            }
        });

        NewsModel.GetIdsCallback callback = mock(NewsModel.GetIdsCallback.class);
        newsData.getIds(callback);

        ArgumentCaptor<Throwable> argumentCaptor = ArgumentCaptor.forClass(Throwable.class);
        verify(callback).onErrorResponse(argumentCaptor.capture());
    }

    @Test
    public void testGetItemsOK(){
        HackerNewsApi hackerNewsApi = mock(HackerNewsApi.class);
        NewsData newsData = new NewsData(hackerNewsApi);
        final int newsId = 0;

        when(hackerNewsApi.getNews(anyInt())).thenAnswer(new Answer<Call<NewsItem>>() {
            @Override
            public Call<NewsItem> answer(InvocationOnMock invocation) throws Throwable {

                Call<NewsItem> call = mock(Call.class);

                doAnswer(
                        new Answer() {
                            @Override
                            public Object answer(InvocationOnMock invocation) throws Throwable {
                                NewsItem newsItem = new NewsItem();
                                newsItem.id = newsId;
                                Response response = Response.success(newsItem);
                                ((Callback)invocation.getArgument(0)).onResponse(null,response);
                                return null;
                            }
                        }
                ).when(call).enqueue(any(Callback.class));
                return call;
            }
        });


        NewsModel.GetNewsItemCallback callback = mock(NewsModel.GetNewsItemCallback.class);
        newsData.getItem(newsId,callback);

        ArgumentCaptor<NewsItem> argumentCaptor = ArgumentCaptor.forClass(NewsItem.class);
        verify(callback).onResponse(argumentCaptor.capture());

        assertEquals(newsId,argumentCaptor.getValue().id);
    }

    @Test
    public void testGetItemsFailed(){

        HackerNewsApi hackerNewsApi = mock(HackerNewsApi.class);
        NewsData newsData = new NewsData(hackerNewsApi);
        final int newsId = 0;

        when(hackerNewsApi.getNews(anyInt())).thenAnswer(new Answer<Call<NewsItem>>() {
            @Override
            public Call<NewsItem> answer(InvocationOnMock invocation) throws Throwable {

                Call<NewsItem> call = mock(Call.class);

                doAnswer(
                        new Answer() {
                            @Override
                            public Object answer(InvocationOnMock invocation) throws Throwable {

                                ((Callback)invocation.getArgument(0)).onFailure(null,new Throwable("error"));
                                return null;
                            }
                        }
                ).when(call).enqueue(any(Callback.class));
                return call;
            }
        });

        NewsModel.GetNewsItemCallback callback = mock(NewsModel.GetNewsItemCallback.class);
        newsData.getItem(newsId,callback);

        ArgumentCaptor<Throwable> argumentCaptor = ArgumentCaptor.forClass(Throwable.class);
        verify(callback).onErrorResponse(argumentCaptor.capture());
    }
}
