package com.propertygurutest.hackernewsreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.propertygurutest.hackernewsreader.util.UILoader;

/**
 * Created by HJ Chin on 15/11/2017.
 */

public class UILoaderActivity extends AppCompatActivity {

    UILoader uiLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loader_test);
        View loadingContainer = findViewById(R.id.loading_container);
        View contentContainer = findViewById(R.id.news);
        uiLoader = new UILoader(this, loadingContainer,contentContainer);
    }
}
