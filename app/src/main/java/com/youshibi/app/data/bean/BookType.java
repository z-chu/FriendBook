package com.youshibi.app.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Chu on 2017/5/19.
 */

public class BookType implements Serializable, Parcelable {

    @SerializedName("Id")
    private long id;

    @SerializedName("TypeName")
    private String typeName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.typeName);
    }

    public BookType() {
    }

    protected BookType(Parcel in) {
        this.id = in.readLong();
        this.typeName = in.readString();
    }

    public static final Parcelable.Creator<BookType> CREATOR = new Parcelable.Creator<BookType>() {
        @Override
        public BookType createFromParcel(Parcel source) {
            return new BookType(source);
        }

        @Override
        public BookType[] newArray(int size) {
            return new BookType[size];
        }
    };
}
