package com.youshibi.app.data.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chu on 2017/5/28.
 */

public class BookSectionContent {

    /**
     * ChapterIndex : 1
     * ChapterName : 1.第1章 惨遭遗弃
     * ChapterContent : ..
     * CreateDateTime : 2017-03-28T23:30:43.01
     */

    @SerializedName("ChapterIndex")
    private int sectionIndex;

    @SerializedName("ChapterName")
    private String sectionName;

    @SerializedName("ChapterContent")
    private String content;

    @SerializedName("CreateDateTime")
    private String createDateTime;

    public int getSectionIndex() {
        return sectionIndex;
    }

    public void setSectionIndex(int sectionIndex) {
        this.sectionIndex = sectionIndex;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }
}
