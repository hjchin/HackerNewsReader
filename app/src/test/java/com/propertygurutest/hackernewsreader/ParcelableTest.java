package com.propertygurutest.hackernewsreader;

/**
 * Created by HJ Chin on 16/11/2017.
 */


import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;

/**
 * Created by HJ Chin on 16/11/2017.
 *
 */
@RunWith(RobolectricTestRunner.class)
public class ParcelableTest {

    @Test
    public void testNewsItemParcelable(){
        ParcelableActivity activity = Robolectric.setupActivity(ParcelableActivity.class);

        NewsItem newsItem = new NewsItem();
        newsItem.by = "by";
        newsItem.title = "title";
        newsItem.type = "type";
        newsItem.url = "url";

        NewsItem createFromParcel = activity.getFromParcel(newsItem);

        assertEquals(newsItem.by,createFromParcel.by);
        assertEquals(newsItem.title,createFromParcel.title);
        assertEquals(newsItem.type,createFromParcel.type);
        assertEquals(newsItem.url,createFromParcel.url);
    }

    @Test public void testCommentItemParcelable(){

        ParcelableActivity activity = Robolectric.setupActivity(ParcelableActivity.class);

        CommentItem commentItem = new CommentItem();
        commentItem.by = "by";
        commentItem.id = 123;
        commentItem.parent = 1234;
        commentItem.text = "text";
        commentItem.time = 123L;
        commentItem.type = "type";

        CommentItem createFromParcel = activity.getFromParcel(commentItem);

        assertEquals(commentItem.by,createFromParcel.by);
        assertEquals(commentItem.id,createFromParcel.id);
        assertEquals(commentItem.parent,createFromParcel.parent);
        assertEquals(commentItem.text,createFromParcel.text);
        assertEquals(commentItem.time,createFromParcel.time);
        assertEquals(commentItem.type,createFromParcel.type);
    }
}
