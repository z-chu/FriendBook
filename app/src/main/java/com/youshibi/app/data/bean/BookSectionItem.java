package com.youshibi.app.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Chu on 2017/5/28.
 */

public class BookSectionItem implements Serializable {
    /**
     * "ChapterIndex": 2181,
     * "ChapterName": "2181.第2181章 终极神话"
     */

    @SerializedName("chapter_index")
    private int sectionIndex;

    @SerializedName("chapter_name")
    private String sectionName;

    @SerializedName("chapter_id")
    private String sectionId;

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

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
}
