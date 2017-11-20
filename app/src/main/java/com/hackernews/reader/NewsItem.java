package com.hackernews.reader;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HJ Chin on 11/11/2017.
 *
 */

public class NewsItem implements Parcelable {
    String by;
    int descendants;
    int id;
    int[] kids;
    int score;
    long time;
    String title;
    String type;
    String url;

    NewsItem(){
    }

    private NewsItem(Parcel in) {
        by = in.readString();
        descendants = in.readInt();
        id = in.readInt();
        kids = in.createIntArray();
        score = in.readInt();
        time = in.readLong();
        title = in.readString();
        type = in.readString();
        url = in.readString();
    }

    static final Creator<NewsItem> CREATOR = new Creator<NewsItem>() {
        @Override
        public NewsItem createFromParcel(Parcel in) {
            return new NewsItem(in);
        }

        @Override
        public NewsItem[] newArray(int size) {
            return new NewsItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(by);
        parcel.writeInt(descendants);
        parcel.writeInt(id);
        parcel.writeIntArray(kids);
        parcel.writeInt(score);
        parcel.writeLong(time);
        parcel.writeString(title);
        parcel.writeString(type);
        parcel.writeString(url);
    }
}
