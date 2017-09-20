package com.youshibi.app.data.bean;

import com.google.gson.annotations.SerializedName;

/**
 * author : zchu
 * date   : 2017/9/20
 * desc   :
 */

public class BookDetail {

    private Book book;

    @SerializedName("chapter_count")
    private int chapterCount;

    @SerializedName("last_chapter")
    private LatestChapter latestChapter;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getChapterCount() {
        return chapterCount;
    }

    public void setChapterCount(int chapterCount) {
        this.chapterCount = chapterCount;
    }

    public LatestChapter getLatestChapter() {
        return latestChapter;
    }

    public void setLatestChapter(LatestChapter latestChapter) {
        this.latestChapter = latestChapter;
    }


}
