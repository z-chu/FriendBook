package com.youshibi.app.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Chu on 2017/5/28.
 */

public class BookSectionContent implements Serializable{

    /**
     * ChapterIndex : 1
     * ChapterName : 1.第1章 惨遭遗弃
     * ChapterContent : ..
     * CreateDateTime : 2017-03-28T23:30:43.01
     */

    @SerializedName("chapter_index")
    private int sectionIndex;

    @SerializedName("chapter_name")
    private String sectionName;

    @SerializedName("chapter_content")
    private String content;

    @SerializedName("create_date_time")
    private String createDateTime;

    @SerializedName("chapter_id")
    private String sectionId;

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

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
}
