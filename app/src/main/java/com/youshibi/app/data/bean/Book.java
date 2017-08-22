package com.youshibi.app.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Chu on 2016/12/3.
 */

public class Book implements Serializable, Parcelable {
    /**
     * BookId : 0a4d9876-fa33-4bfa-a76f-3f2833674745
     * BookName : 混沌战尊
     * BookAuthor : 作者：蓝色蝌蚪
     * BookOneType : 6
     * BookOneTypeName : 玄幻小说
     * BookIntro : 意外来到异世，获得阴阳混沌决，他需要让自己强大起来，来解开封印的功法，笑看异世风云，拥红颜知己，战天下群豪。
     * BookImg : http://pic.quanshuwu.com/files/book/1/3954/201610100732144489.jpg
     * IsFinish : true
     * BookWordNum : 3353200
     */


    @SerializedName("BookId")
    private String id;

    @SerializedName("BookImg")
    private String coverUrl;

    @SerializedName("BookName")
    private String name;

    @SerializedName("BookIntro")
    private String describe;

    @SerializedName("BookAuthor")
    private String author;

    @SerializedName("IsFinish")
    private boolean isFinished;

    private int BookOneType;

    private String BookOneTypeName;

    private long BookWordNum;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public int getBookOneType() {
        return BookOneType;
    }

    public void setBookOneType(int bookOneType) {
        BookOneType = bookOneType;
    }

    public String getBookOneTypeName() {
        return BookOneTypeName;
    }

    public void setBookOneTypeName(String bookOneTypeName) {
        BookOneTypeName = bookOneTypeName;
    }

    public long getBookWordNum() {
        return BookWordNum;
    }

    public void setBookWordNum(long bookWordNum) {
        BookWordNum = bookWordNum;
    }


    public Book() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.coverUrl);
        dest.writeString(this.name);
        dest.writeString(this.describe);
        dest.writeString(this.author);
        dest.writeByte(this.isFinished ? (byte) 1 : (byte) 0);
        dest.writeInt(this.BookOneType);
        dest.writeString(this.BookOneTypeName);
        dest.writeLong(this.BookWordNum);
    }

    protected Book(Parcel in) {
        this.id = in.readString();
        this.coverUrl = in.readString();
        this.name = in.readString();
        this.describe = in.readString();
        this.author = in.readString();
        this.isFinished = in.readByte() != 0;
        this.BookOneType = in.readInt();
        this.BookOneTypeName = in.readString();
        this.BookWordNum = in.readLong();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
