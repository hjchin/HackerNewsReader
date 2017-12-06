package com.hackernews.reader.data.comment;

import com.android.volley.VolleyError;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
        CommentItemRequest request = mock(CommentItemRequest.class);
        CommentData commentData = new CommentData(request);
        commentData.fill(new int[]{0,1,2,3,4,5,6,7,8,9});
        ArrayList<CommentItem> list = commentData.getImmutableList();
        assertEquals(10,list.size());
        for(int i=0;i<list.size();i++){
            assertEquals(i,list.get(i).id);
        }
    }

    @Test public void testFillObject(){
        CommentItemRequest request = mock(CommentItemRequest.class);
        CommentData commentData = new CommentData(request);
        commentData.fill(commentItems);
        ArrayList<CommentItem> list = commentData.getImmutableList();
        assertEquals(10,list.size());
        for(int i=0;i<list.size();i++){
            assertEquals(commentItems.get(i).id,list.get(i).id);
        }
    }

    @Test public void testGetItemSuccessfully(){

        CommentItemRequest request = mock(CommentItemRequest.class);
        final CommentData commentData = new CommentData(request);

        doAnswer(
                new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        int index = (int)invocation.getArgument(0);
                        CommentItemRequest.Callback callback = (CommentItemRequest.Callback)invocation.getArgument(1);
                        callback.onResponse(commentItems.get(index));
                        return null;
                    }
                }
        ).when(request).requestItem(anyInt(),any(CommentItemRequest.Callback.class));

        CommentModel.GetItemCallback callback = mock(CommentModel.GetItemCallback.class);
        commentData.getItem(0,callback);

        ArgumentCaptor<CommentItem> argumentCaptor = ArgumentCaptor.forClass(CommentItem.class);
        verify(callback).onResponse(argumentCaptor.capture());

        assertEquals(commentItems.get(0),argumentCaptor.getValue());
    }

    @Test public void testGetItemFail(){

        CommentItemRequest request = mock(CommentItemRequest.class);
        final CommentData commentData = new CommentData(request);

        doAnswer(
                new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        int index = (int)invocation.getArgument(0);
                        CommentItemRequest.Callback callback = (CommentItemRequest.Callback)invocation.getArgument(1);
                        callback.onErrorResponse(new VolleyError("dummy error"));
                        return null;
                    }
                }
        ).when(request).requestItem(anyInt(),any(CommentItemRequest.Callback.class));

        CommentModel.GetItemCallback callback = mock(CommentModel.GetItemCallback.class);
        commentData.getItem(0,callback);

        ArgumentCaptor<VolleyError> argumentCaptor = ArgumentCaptor.forClass(VolleyError.class);
        verify(callback).onErrorResponse(argumentCaptor.capture());

        assertEquals("dummy error",argumentCaptor.getValue().getMessage());
    }

}
