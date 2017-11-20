package com.hackernews.reader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.Assert.assertEquals;

/**
 * Created by HJ Chin on 17/11/2017.
 *
 */

@RunWith(RobolectricTestRunner.class)
public class CommentAdapterTest {

    @Test
    public void testCommentAdapter(){

        CommentAdapterActivity activity = Robolectric.setupActivity(CommentAdapterActivity.class);

        ArrayList<CommentItem> item = new ArrayList<>();
        CommentItem commment1 = new CommentItem();
        commment1.by = "by1";
        commment1.id = 123;
        //commment1.kids
        commment1.parent = 1234;
        commment1.text = "text1";
        commment1.time = ((new Date()).getTime())/1000;
        commment1.type = "comment";
        item.add(commment1);

        CommentItem commment2 = new CommentItem();
        //commment2.by = "by1";
        commment2.id = 123;
        commment2.kids = new int[]{1};
        commment2.parent = 1234;
        commment2.text = "text1";
        commment2.time = ((new Date()).getTime())/1000;
        commment2.type = "comment";
        item.add(commment2);

        CommentItem commment3 = new CommentItem();
        //commment2.by = "by1";
        commment3.id = 123;
        commment3.kids = new int[]{1,2,3};
        commment3.parent = 1234;
        commment3.text = "text1";
        commment3.time = ((new Date()).getTime())/1000;
        commment3.type = "comment";
        item.add(commment3);

        CommentItem commment4 = new CommentItem();
        commment4.by = "by4";
        commment4.id = 123;
        commment4.kids = new int[]{};
        commment4.parent = 1234;
        commment4.text = "text1";
        commment4.time = ((new Date()).getTime())/1000;
        commment4.type = "comment";
        item.add(commment4);


        CommentItem commment5 = new CommentItem();
        commment5.by = "by4";
        commment5.id = 123;
        commment5.kids = new int[]{};
        commment5.parent = 1234;
        //commment5.text = "text1";
        commment5.time = ((new Date()).getTime())/1000;
        commment5.type = "comment";
        item.add(commment5);

        activity.setCommentItem(item);
        assertEquals(item.size(),activity.adapter.getItemCount());
        assertEquals(item.get(0),activity.adapter.getItemAt(0));
    }
}
