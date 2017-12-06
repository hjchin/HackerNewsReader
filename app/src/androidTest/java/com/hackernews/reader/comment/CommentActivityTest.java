package com.hackernews.reader.comment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.hackernews.reader.R;
import com.hackernews.reader.data.comment.CommentProvider;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by HJ Chin on 2/12/2017.
 */

@RunWith(AndroidJUnit4.class)
public class CommentActivityTest {


    private ActivityTestRule<CommentActivity> activityTestRule = new ActivityTestRule<>(CommentActivity.class, true, false);
    private Context context;

    @Before
    public void setup(){
        context = InstrumentationRegistry.getTargetContext();
    }

    private Activity launchActivity(Intent intent){
        CommentProvider.getInstance(null).connect();
        CommentActivity activity = activityTestRule.launchActivity(intent);
        IdlingRegistry.getInstance().register(activity.getIdlingResource());
        return activity;
    }

    @Test public void testNullComment(){
        launchActivity(null);
        onView(withId(R.id.no_comment)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyComment(){
        Intent intent = new Intent();
        intent.putExtra(CommentActivity.COMMENT_LIST,new int[0]);
        launchActivity(intent);
        onView(withId(R.id.no_comment)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoadComment(){
        Intent intent = new Intent();
        intent.putExtra(CommentActivity.COMMENT_LIST,createCommentIds());
        launchActivity(intent);
        onView(withText(context.getString(R.string.comment_text)+ " 0")).check(matches(isDisplayed()));
    }

    @Test
    public void testClickReply(){
        Intent intent = new Intent();
        intent.putExtra(CommentActivity.COMMENT_LIST,createCommentIds());
        launchActivity(intent);
        onView(withId(R.id.comment_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.kid)));
        onView(withId(R.id.comment_list)).check(matches(isDisplayed()));
        onView(withText(context.getString(R.string.comment_text)+ " 0")).check(matches(isDisplayed()));
    }

    @Test
    public void testRotateScreen(){
        Intent intent = new Intent();
        intent.putExtra(CommentActivity.COMMENT_LIST,createCommentIds());
        Activity activity = launchActivity(intent);
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        onView(withText(context.getString(R.string.comment_text)+" 0")).check(matches(isDisplayed()));
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        onView(withText(context.getString(R.string.comment_text)+" 0")).check(matches(isDisplayed()));
    }

    @Test
    public void testOffline(){

        ActivityTestRule<CommentActivity> activityTestRule = new ActivityTestRule<CommentActivity>(CommentActivity.class, true, false){
            @Override
            protected void afterActivityLaunched() {
                super.afterActivityLaunched();
                CommentProvider.getInstance(null).disconnect();
            }
        };

        Intent intent = new Intent();
        intent.putExtra(CommentActivity.COMMENT_LIST, createCommentIds());
        CommentActivity activity = activityTestRule.launchActivity(intent);
        IdlingRegistry.getInstance().register(activity.getIdlingResource());

        onView(withId(R.id.retry_button)).check(matches(isDisplayed()));

        CommentProvider.getInstance(null).connect();

        onView(withId(R.id.retry_button)).perform(click());
        onView(withId(R.id.comment_list)).check(matches(isDisplayed()));
    }

    private int[] createCommentIds() {
        return new int[]{0,1,2,3,4,5,6,7,8,9};
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }
}