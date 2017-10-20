package com.youshibi.app.data.bean;

import com.google.gson.annotations.SerializedName;

/**
 * author : zchu
 * date   : 2017/10/20
 * desc   :
 */

public class LatestChapter {

    @SerializedName("book_id")
    private String bookId;

    @SerializedName("chapter_count")
    private int chapterCount;

    @SerializedName("last_chapter")
    private BookChapter latestChapter;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
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
