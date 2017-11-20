package com.hackernews.reader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by HJ Chin on 20/11/2017.
 */

@RunWith(AndroidJUnit4.class)
public class NoNetworkNewsTest {

    @Rule
    public final ActivityTestRule<NewsActivity> activityTestRule = new ActivityTestRule<>(NewsActivity.class);
    private CountingIdlingResource countingIdlingResource;
    private NewsActivity activity;

    @Before
    public void setup(){
        activity = activityTestRule.getActivity();
        countingIdlingResource = activity.getIdlingResource();
        IdlingRegistry.getInstance().getResources().clear();
        IdlingRegistry.getInstance().register(countingIdlingResource);
        setWifiEnabled(false);
    }

    @Test
    public void test(){
        testNoNetworkAtFirstLoad();
        testNoNetworkDuringScrolling();
    }

    public void testNoNetworkAtFirstLoad(){
        activityTestRule.launchActivity(new Intent());
        onView(withId(R.id.retry_button)).check(matches(isDisplayed()));
        setWifiEnabled(true);
    }

    private void testNoNetworkDuringScrolling(){
        onView(withId(R.id.retry_button)).perform(click());
        onView(withId(R.id.news)).check(matches(isDisplayed()));
        onView(withId(R.id.news)).perform(scrollToPosition(50));
        setWifiEnabled(false);
        onView(withId(R.id.retry_button)).check(matches(isDisplayed()));
        setWifiEnabled(true);
        onView(withId(R.id.retry_button)).perform(click());
        onView(withId(R.id.news)).check(matches(isDisplayed()));
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

    @After
    public void restore(){
        activityTestRule.finishActivity();

        setWifiEnabled(true);

        if(countingIdlingResource != null){
            IdlingRegistry.getInstance().unregister(countingIdlingResource);
            IdlingRegistry.getInstance().getResources().clear();
        }
    }
}
