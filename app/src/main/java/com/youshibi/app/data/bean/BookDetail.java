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
    private BookChapter latestChapter;

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

    public BookChapter getLatestChapter() {
        return latestChapter;
    }

    public void setLatestChapter(BookChapter latestChapter) {
        this.latestChapter = latestChapter;
    }


}
