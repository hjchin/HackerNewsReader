package com.hackernews.reader.data.comment;

import com.android.volley.VolleyError;
import com.hackernews.reader.data.RestApiInterface;
import com.hackernews.reader.data.comment.mock.FakeBrokenCommentRestApi;
import com.hackernews.reader.data.comment.mock.FakeCommentRestApi;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

/**
 * Created by HJ Chin on 5/12/2017.
 */

public class CommentItemRequestTest {

    @Test
    public void testOKApiRequest(){
        int commentId = 0;

        RestApiInterface api = new FakeCommentRestApi(false);
        CommentItemRequest commentItemRequest = new CommentItemRequest(api);
        CommentItemRequest.Callback callback = mock(CommentItemRequest.Callback.class);
        commentItemRequest.requestItem(commentId, callback);

        ArgumentCaptor<CommentItem> argumentCaptor = ArgumentCaptor.forClass(CommentItem.class);
        verify(callback, timeout(50000)).onResponse(argumentCaptor.capture());
        CommentItem commentItem = argumentCaptor.getValue();
        assertEquals(commentId,commentItem.id);
    }

    @Test
    public void testOKApiRequestWithModificationOnData(){
        int commentId = 11;

        RestApiInterface api = new FakeBrokenCommentRestApi(false);
        CommentItemRequest commentItemRequest = new CommentItemRequest(api);
        CommentItemRequest.Callback callback = mock(CommentItemRequest.Callback.class);
        commentItemRequest.requestItem(commentId, callback);

        ArgumentCaptor<CommentItem> argumentCaptor = ArgumentCaptor.forClass(CommentItem.class);
        verify(callback, timeout(50000)).onResponse(argumentCaptor.capture());
        CommentItem commentItem = argumentCaptor.getValue();
        assertEquals(commentId,commentItem.id);
        assertEquals("",commentItem.by);
    }

    @Test
    public void testFailedApiRequest() {
        int commentId = 0;
        RestApiInterface api = new FakeCommentRestApi(true);
        CommentItemRequest commentItemRequest = new CommentItemRequest(api);
        CommentItemRequest.Callback callback = mock(CommentItemRequest.Callback.class);
        commentItemRequest.requestItem(commentId, callback);
        ArgumentCaptor<VolleyError> argumentCaptor = ArgumentCaptor.forClass(VolleyError.class);
        verify(callback,timeout(50000)).onErrorResponse(argumentCaptor.capture());
    }

}
