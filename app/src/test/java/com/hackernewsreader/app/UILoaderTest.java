package com.propertygurutest.hackernewsreader;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.propertygurutest.hackernewsreader.util.UILoader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.stubbing.OngoingStubbing;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by HJ Chin on 12/11/2017.
 *
 * Robolectric support API 16 and onwards,
 * hence API 11 to API 15 has to be test on real device/simulator
 */


@RunWith(RobolectricTestRunner.class)
//@Config(
//    minSdk = 11, maxSdk = 25
//)
public class UILoaderTest {

    @Test
    public void testShowLoader(){
          UILoaderActivity activity = Robolectric.setupActivity(UILoaderActivity.class);
          activity.uiLoader.showLoader();
          assertThat(1f, equalTo(activity.findViewById(R.id.loading_container).getAlpha()));
          assertThat(0f, equalTo(activity.findViewById(R.id.news).getAlpha()));
    }

    @Test
    public void testShowContent(){
        UILoaderActivity activity = Robolectric.setupActivity(UILoaderActivity.class);
        activity.uiLoader.showContent();
        assertThat(0f, equalTo(activity.findViewById(R.id.loading_container).getAlpha()));
        assertThat(1f, equalTo(activity.findViewById(R.id.news).getAlpha()));
    }

    @Test public void testShowError() {

        final UILoaderActivity activity = Robolectric.setupActivity(UILoaderActivity.class);
        activity.uiLoader.showLoadError("error", new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                ((TextView)activity.findViewById(R.id.error_message)).setText("button clicked");
            }
        });

        assertEquals(View.VISIBLE, activity.findViewById(R.id.loading_error_container).getVisibility());
        assertEquals("error", ((TextView) activity.findViewById(R.id.error_message)).getText().toString());

        activity.findViewById(R.id.retry_button).performClick();
        assertEquals("button clicked", ((TextView) activity.findViewById(R.id.error_message)).getText().toString());
    }
}
