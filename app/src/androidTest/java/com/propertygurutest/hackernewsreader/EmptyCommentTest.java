package com.propertygurutest.hackernewsreader;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.propertygurutest.hackernewsreader.CommentActivity.COMMENT_LIST;

/**
 * Created by HJ Chin on 19/11/2017.
 */

@RunWith(AndroidJUnit4.class)
public class EmptyCommentTest {

    @Rule
    public ActivityTestRule<CommentActivity> activityTestRule = new ActivityTestRule<>(CommentActivity.class, true, false);

    @Test
    public void testNullComment(){
        activityTestRule.launchActivity(new Intent());
        onView(withId(R.id.no_comment)).check(matches(isDisplayed()));
        activityTestRule.finishActivity();
    }

    @Test
    public void testEmptyComment(){
        Intent data = new Intent();
        data.putExtra(COMMENT_LIST, new int[0]);
        activityTestRule.launchActivity(data);
        onView(withId(R.id.no_comment)).check(matches(isDisplayed()));
        activityTestRule.finishActivity();
    }

}
