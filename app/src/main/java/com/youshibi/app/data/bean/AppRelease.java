package com.youshibi.app.data.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chu on 2017/8/29.
 */

public class AppRelease {

    /**
     * version_code : 1
     * version_name : v1.01
     * version_desc : 1.更新了***页面样 2.修复了***BUG
     * source_file_url : http://android.laiyoushu.com/download/v1.01
     */

    @SerializedName("version_code")
    private int versionCode;

    @SerializedName("version_name")
    private String versionName;

    @SerializedName("release_notes")
    private String releaseNotes;

    @SerializedName("source_file_url")
    private String sourceFileUrl;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }

    public String getSourceFileUrl() {
        return sourceFileUrl;
    }

    public void setSourceFileUrl(String sourceFileUrl) {
        this.sourceFileUrl = sourceFileUrl;
    }
}
