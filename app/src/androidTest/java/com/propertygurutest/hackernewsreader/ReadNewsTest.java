package com.propertygurutest.hackernewsreader;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.propertygurutest.hackernewsreader.util.AppLog;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Iterator;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.internal.util.Checks.checkMainThread;
import static android.support.test.internal.util.Checks.checkNotNull;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsMapContaining.hasEntry;

/**
 * Created by HJ Chin on 11/11/2017.
 *
 */

@RunWith(AndroidJUnit4.class)
public class ReadNewsTest {

    @Rule
    public ActivityTestRule<NewsActivity> activityTestRule = new ActivityTestRule<>(NewsActivity.class);
    private CountingIdlingResource countingIdlingResource;
    private NewsActivity activity;
    private int nonZeroCommentIndex = -1;

    @Before
    public void setup() {
        activity = activityTestRule.getActivity();
        countingIdlingResource = activity.getIdlingResource();
        IdlingRegistry.getInstance().getResources().clear();
        IdlingRegistry.getInstance().register(countingIdlingResource);
    }

    @Test
    public void test() {
        testDisplayResult();
        testRotation();
        testSwipeToRefresh();
        testClickNewsItem();
        testCommentOrientation();
        testViewReply();
        //testCommentNoNetwork();
    }

    private void testDisplayResult() {
        onView(withId(R.id.news)).check(matches(isDisplayed()));
    }

    private void testRotation(){
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        onView(withId(R.id.news)).check(matches(isDisplayed()));
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        onView(withId(R.id.news)).check(matches(isDisplayed()));
    }

    private void testSwipeToRefresh(){
        onView(withId(R.id.swipe_refresh)).perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)));
        RecyclerView recyclerView =(RecyclerView)activity.findViewById(R.id.news);
        assertTrue(recyclerView.getAdapter().getItemCount()>0);
    }

    private void testClickNewsItem(){
        onView(withId(R.id.news)).check(matches(isDisplayed()));

        RecyclerView recyclerView = (RecyclerView)activity.findViewById(R.id.news);
        nonZeroCommentIndex = -1;

        for(int i=0;i<10;i++){
            onView(withId(R.id.news)).perform(scrollToPosition(i));
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int counter = 0;

        do{
            for(int i=0;i<recyclerView.getAdapter().getItemCount();i++){
                NewsItem newsItem = ((NewsAdapter)recyclerView.getAdapter()).getItemAt(i);
                if(newsItem.kids != null && newsItem.kids.length > 0){
                   if(nonZeroCommentIndex == -1){
                       nonZeroCommentIndex = i;
                       break;
                   }
                }
            }

            counter++;
            //System.out.println("nonZeroCommentIndex "+nonZeroCommentIndex);
        }while(nonZeroCommentIndex == -1 && counter <4);

        if(nonZeroCommentIndex == -1){
            nonZeroCommentIndex = 0;
        }

        onView(withId(R.id.news)).perform(
                actionOnItemAtPosition(nonZeroCommentIndex,click())
        );

        onView(withId(R.id.comment_list)).check(matches(isDisplayed()));
    }

    private void testCommentOrientation(){
        CommentActivity commentActivity = (CommentActivity) getActivityInstance();
        commentActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        onView(withId(R.id.comment_list)).check(matches(isDisplayed()));
        commentActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        onView(withId(R.id.comment_list)).check(matches(isDisplayed()));
    }

    private void testViewReply(){

        int nonZeroReplyIndex = -1;
        int Looped = 0;
        boolean replyExists = true;

        do{
            CommentActivity commentActivity = (CommentActivity) getActivityInstance();
            RecyclerView commentRecyclerView  = (RecyclerView)commentActivity.findViewById(R.id.comment_list);

            for(int i=0;i<commentRecyclerView.getAdapter().getItemCount();i++){
                onView(withId(R.id.comment_list)).perform(scrollToPosition(i));
            }

            for(int i=0;i<commentRecyclerView.getAdapter().getItemCount();i++) {
                CommentItem commentItem = ((CommentAdapter)commentRecyclerView.getAdapter()).getItemAt(i);
                if(commentItem.kids != null && commentItem.kids.length > 0){
                    nonZeroReplyIndex = i;
                    break;
                }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Looped++;
            if(Looped == 3){

                //Back to news page and select next comments.
                onView(isRoot()).perform(pressBack());

                int oldNonZeroCommentIndex = nonZeroCommentIndex;
                NewsActivity newsActivity = (NewsActivity) getActivityInstance();
                RecyclerView newsRecyclerView = (RecyclerView) newsActivity.findViewById(R.id.news);

                if(nonZeroCommentIndex == newsRecyclerView.getAdapter().getItemCount()-1){
                    replyExists = false;
                }

                do{
                    for(int i=0;i<newsRecyclerView.getAdapter().getItemCount();i++){
                        NewsItem newsItem = ((NewsAdapter)newsRecyclerView.getAdapter()).getItemAt(i);
                        if(newsItem.kids != null && newsItem.kids.length > 0 && i > nonZeroCommentIndex){
                            nonZeroCommentIndex = i;
                            break;
                        }
                    }
                }while(nonZeroCommentIndex == oldNonZeroCommentIndex && replyExists);

                Looped = 0;

                onView(withId(R.id.news)).perform(
                        actionOnItemAtPosition(nonZeroCommentIndex,click())
                );
            }

        }while(nonZeroReplyIndex == -1 && replyExists);

        if(replyExists){
            onView(withId(R.id.comment_list)).perform(
                    actionOnItemAtPosition(nonZeroReplyIndex,clickChildViewWithId(R.id.kid))
            );

            onView(withId(R.id.comment_list)).check(matches(isDisplayed()));
            onView(isRoot()).perform(pressBack());
        }
    }

//    private void testCommentNoNetwork(){
//        onView(isRoot()).perform(pressBack());
//
//        onView(withId(R.id.news)).perform(
//                actionOnItemAtPosition(0,click())
//        );
//
//        setWifiEnabled(false);
//
//        onView(withId(R.id.retry_button)).check(matches(isDisplayed()));
//
//        setWifiEnabled(true);
//
//        onView(withId(R.id.retry_button)).perform(click());
//
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        onView(withId(R.id.comment_list)).check(matches(isDisplayed()));
//    }

    public static ViewAction withCustomConstraints(final ViewAction action, final Matcher<View> constraints) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return constraints;
            }

            @Override
            public String getDescription() {
                return action.getDescription();
            }

            @Override
            public void perform(UiController uiController, View view) {
                action.perform(uiController, view);
            }
        };
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

    @After
    public void unregisterIdlingResource() {
        if (countingIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(countingIdlingResource);
        }
        IdlingRegistry.getInstance().getResources().clear();

        setWifiEnabled(true);
    }

    private void setWifiEnabled(boolean value){
        WifiManager wifi = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(value);

        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            if(value){
                while(cm.getActiveNetworkInfo() == null){
                }
            }else{
                while(cm.getActiveNetworkInfo() != null){
                }
            }
        }
    }

    private Activity getActivityInstance(){
        final Activity[] currentActivity = {null};

        getInstrumentation().runOnMainSync(new Runnable(){
            public void run(){
                Collection<Activity> resumedActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                Iterator<Activity> it = resumedActivity.iterator();
                currentActivity[0] = it.next();
            }
        });

        return currentActivity[0];
    }
}
