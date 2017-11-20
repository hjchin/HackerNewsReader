package com.propertygurutest.hackernewsreader;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HJ Chin on 13/11/2017.
 */

public class CommentItem implements Parcelable{
    String by;
    int id;
    int[] kids;
    int parent;
    String text;
    long time;
    String type;

    CommentItem(){

    }

    protected CommentItem(Parcel in) {
        by = in.readString();
        id = in.readInt();
        kids = in.createIntArray();
        parent = in.readInt();
        text = in.readString();
        time = in.readLong();
        type = in.readString();
    }

    public static final Creator<CommentItem> CREATOR = new Creator<CommentItem>() {
        @Override
        public CommentItem createFromParcel(Parcel in) {
            return new CommentItem(in);
        }

        @Override
        public CommentItem[] newArray(int size) {
            return new CommentItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(by);
        parcel.writeInt(id);
        parcel.writeIntArray(kids);
        parcel.writeInt(parent);
        parcel.writeString(text);
        parcel.writeLong(time);
        parcel.writeString(type);
    }
}
