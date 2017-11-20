package com.propertygurutest.hackernewsreader;

import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class ParcelableActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public NewsItem getFromParcel(NewsItem parcelable) {
        Parcel parcel = Parcel.obtain();
        parcelable.writeToParcel(parcel,parcelable.describeContents());
        parcel.setDataPosition(0);
       return NewsItem.CREATOR.createFromParcel(parcel);
    }

    public CommentItem getFromParcel(CommentItem parcelable) {
        Parcel parcel = Parcel.obtain();
        parcelable.writeToParcel(parcel,parcelable.describeContents());
        parcel.setDataPosition(0);
        return CommentItem.CREATOR.createFromParcel(parcel);
    }

}
