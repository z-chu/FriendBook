package com.youshibi.app.data.bean;

import com.google.gson.annotations.SerializedName;

/**
 * author : zchu
 * date   : 2017/9/20
 * desc   :
 */

public class BookChapter {

    /**
     * chapter_id : d6771dd9-05da-4aa6-9344-0017ee8623cf
     * chapter_index : 3127
     * chapter_name : 第3119章 岂不是又纠缠不清了？
     * create_time : 2017-04-02T10:51:45.367
     */

    @SerializedName("chapter_id")
    private String chapterId;

    @SerializedName("chapter_index")
    private int chapterIndex;

    @SerializedName("chapter_name")
    private String chapterName;

    @SerializedName("create_time")
    private long createTime;

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public int getChapterIndex() {
        return chapterIndex;
    }

    public void setChapterIndex(int chapterIndex) {
        this.chapterIndex = chapterIndex;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
