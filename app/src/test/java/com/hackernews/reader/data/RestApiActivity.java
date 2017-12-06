package com.hackernews.reader.data;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by HJ Chin on 5/12/2017.
 */

public class RestApiActivity extends AppCompatActivity {

    RestApi api;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = RestApi.getInstance(this);
    }
}
