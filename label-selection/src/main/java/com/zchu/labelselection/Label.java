package com.zchu.labelselection;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * author : zchu
 * date   : 2017/7/10
 * desc   :
 */

public class Label implements Serializable, Parcelable {

    public Label(long id, String name) {
        this.id = id;
        this.name = name;
    }

    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
    }

    public Label() {
    }

    protected Label(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
    }

    public static final Creator<Label> CREATOR = new Creator<Label>() {
        @Override
        public Label createFromParcel(Parcel source) {
            return new Label(source);
        }

        @Override
        public Label[] newArray(int size) {
            return new Label[size];
        }
    };
}
