package com.youshibi.app.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Chu on 2017/5/19.
 */

public class BookType implements Serializable, Parcelable {

    public static final int STATUS_COMMON= 0;
    public static final int STATUS_DEFAULT_SELECTED = 1;
    public static final int STATUS_ALWAYS_SELECTED = 2;

    public BookType() {
    }

    public BookType(long id, String typeName, int selectedStatus) {
        this.id = id;
        this.typeName = typeName;
        this.selectedStatus = selectedStatus;
    }

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String typeName;

    @SerializedName("selected_status")
    private int selectedStatus;

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

    public int getSelectedStatus() {
        return selectedStatus;
    }

    public void setSelectedStatus(int selectedStatus) {
        this.selectedStatus = selectedStatus;
    }

    public boolean isUnselected() {
        return selectedStatus == 0;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BookType) {
            return ((BookType) obj).getId() == id;
        }
        return false;
    }
    @Override
    public int hashCode() {
        return Integer.parseInt(String.valueOf(id));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.typeName);
        dest.writeInt(this.selectedStatus);
    }

    protected BookType(Parcel in) {
        this.id = in.readLong();
        this.typeName = in.readString();
        this.selectedStatus = in.readInt();
    }

    public static final Creator<BookType> CREATOR = new Creator<BookType>() {
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
