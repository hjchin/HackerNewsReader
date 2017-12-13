package com.hackernews.reader.data.comment;

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
 * Created by HJ Chin on 4/12/2017.
 *
 */

public class CommentDataTest {

    static ArrayList<CommentItem> commentItems = new ArrayList<>();

    static{
        for(int i=0;i<10;i++){
            CommentItem item = new CommentItem();
            item.id = i;
            commentItems.add(item);
        }
    }

    @Test
    public void testFillInteger(){
        HackerNewsApi api = mock(HackerNewsApi.class);
        CommentData commentData = new CommentData(api);
        commentData.fill(new int[]{0,1,2,3,4,5,6,7,8,9});
        ArrayList<CommentItem> list = commentData.getImmutableList();
        assertEquals(10,list.size());
        for(int i=0;i<list.size();i++){
            assertEquals(i,list.get(i).id);
        }
    }

    @Test public void testFillObject(){
        HackerNewsApi api = mock(HackerNewsApi.class);
        CommentData commentData = new CommentData(api);
        commentData.fill(commentItems);
        ArrayList<CommentItem> list = commentData.getImmutableList();
        assertEquals(10,list.size());
        for(int i=0;i<list.size();i++){
            assertEquals(commentItems.get(i).id,list.get(i).id);
        }
    }

    @Test public void testGetItemSuccessfully(){

        HackerNewsApi api = mock(HackerNewsApi.class);
        CommentData commentData = new CommentData(api);
        final int commentId = 0;

        when(api.getCommentItem(anyInt())).thenAnswer(new Answer<Call<CommentItem>>(){

            @Override
            public Call<CommentItem> answer(InvocationOnMock invocation) throws Throwable {

                Call<CommentItem> call = mock(Call.class);

                doAnswer(new Answer() {
                             @Override
                             public Object answer(InvocationOnMock invocation) throws Throwable {

                                 ((Callback)invocation.getArgument(0)).onResponse(null, Response.success(commentItems.get(commentId)));
                                 return null;
                             }
                         }

                ).when(call).enqueue(any(Callback.class));

                return call;
            }
        });

        CommentModel.GetItemCallback callback = mock(CommentModel.GetItemCallback.class);
        commentData.getItem(0,callback);

        ArgumentCaptor<CommentItem> argumentCaptor = ArgumentCaptor.forClass(CommentItem.class);
        verify(callback).onResponse(argumentCaptor.capture());

        assertEquals(commentItems.get(commentId),argumentCaptor.getValue());
    }

    @Test public void testGetItemFail(){

        HackerNewsApi api = mock(HackerNewsApi.class);
        CommentData commentData = new CommentData(api);
        final int commentId = 0;

        when(api.getCommentItem(anyInt())).thenAnswer(new Answer<Call<CommentItem>>(){

            @Override
            public Call<CommentItem> answer(InvocationOnMock invocation) throws Throwable {

                Call<CommentItem> call = mock(Call.class);

                doAnswer(new Answer() {
                             @Override
                             public Object answer(InvocationOnMock invocation) throws Throwable {

                                 ((Callback)invocation.getArgument(0)).onFailure(null, new Throwable("error"));
                                 return null;
                             }
                         }

                ).when(call).enqueue(any(Callback.class));

                return call;
            }
        });

        CommentModel.GetItemCallback callback = mock(CommentModel.GetItemCallback.class);
        commentData.getItem(commentId,callback);

        ArgumentCaptor<Throwable> argumentCaptor = ArgumentCaptor.forClass(Throwable.class);
        verify(callback).onErrorResponse(argumentCaptor.capture());

    }

}
