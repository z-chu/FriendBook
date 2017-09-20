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


    @SerializedName("book_id")
    private String id;

    @SerializedName("book_img")
    private String coverUrl;

    @SerializedName("book_name")
    private String name;

    @SerializedName("book_intro")
    private String describe;

    @SerializedName("book_author")
    private String author;

    @SerializedName("is_finish")
    private boolean isFinished;

    @SerializedName("book_type_id")
    private int bookTypeId;

    @SerializedName("book_type_name")
    private String bookTypeName;

    @SerializedName("book_word_num")
    private long bookWordNum;

    @SerializedName("click_num")
    private long clickNum;

    @SerializedName("collection_num")
    private long collectionNum;

    @SerializedName("recommend_num")
    private long recommendNum;

    @SerializedName("create_time")
    private String createDateTime;


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

    public int getBookTypeId() {
        return bookTypeId;
    }

    public void setBookTypeId(int bookTypeId) {
        this.bookTypeId = bookTypeId;
    }

    public String getBookTypeName() {
        return bookTypeName;
    }

    public void setBookTypeName(String bookTypeName) {
        this.bookTypeName = bookTypeName;
    }

    public long getBookWordNum() {
        return bookWordNum;
    }

    public void setBookWordNum(long bookWordNum) {
        this.bookWordNum = bookWordNum;
    }

    public long getClickNum() {
        return clickNum;
    }

    public void setClickNum(long clickNum) {
        this.clickNum = clickNum;
    }


    public long getCollectionNum() {
        return collectionNum;
    }

    public void setCollectionNum(long collectionNum) {
        this.collectionNum = collectionNum;
    }

    public long getRecommendNum() {
        return recommendNum;
    }

    public void setRecommendNum(long recommendNum) {
        this.recommendNum = recommendNum;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
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
        dest.writeInt(this.bookTypeId);
        dest.writeString(this.bookTypeName);
        dest.writeLong(this.bookWordNum);
        dest.writeLong(this.clickNum);
        dest.writeLong(this.collectionNum);
        dest.writeLong(this.recommendNum);
        dest.writeString(this.createDateTime);
    }

    protected Book(Parcel in) {
        this.id = in.readString();
        this.coverUrl = in.readString();
        this.name = in.readString();
        this.describe = in.readString();
        this.author = in.readString();
        this.isFinished = in.readByte() != 0;
        this.bookTypeId = in.readInt();
        this.bookTypeName = in.readString();
        this.bookWordNum = in.readLong();
        this.clickNum = in.readLong();
        this.collectionNum = in.readLong();
        this.recommendNum = in.readLong();
        this.createDateTime = in.readString();
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
