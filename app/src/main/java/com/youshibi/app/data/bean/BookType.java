package com.youshibi.app.data.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chu on 2017/5/19.
 */

public class BookType {

    @SerializedName("Id")
    private long id;

    @SerializedName("TypeName")
    private String typeName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
