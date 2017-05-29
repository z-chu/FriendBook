package com.youshibi.app.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Chu on 2016/12/3.
 */

public class Book implements Serializable{
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
    private String title;

    @SerializedName("BookIntro")
    private String describe;

    @SerializedName("BookAuthor")
    private String author;

    @SerializedName("IsFinish")
    private boolean isFinish;

    private int BookOneType;

    private String BookOneTypeName;

    private int BookWordNum;


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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
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

    public int getBookWordNum() {
        return BookWordNum;
    }

    public void setBookWordNum(int bookWordNum) {
        BookWordNum = bookWordNum;
    }
}
