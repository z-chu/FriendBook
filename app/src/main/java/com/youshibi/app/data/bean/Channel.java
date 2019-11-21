package com.youshibi.app.data.bean;

import com.google.gson.annotations.SerializedName;
import com.youshibi.app.presentation.book.ChannelType;

import java.io.Serializable;


/**
 * author : zchu
 * date   : 2017/9/18
 * desc   :
 */

public class Channel implements Serializable {

    /**
     * channel_id : 1
     * channel_name : 精选
     * channel_type : book-ranking
     * content_query_id : featured
     * content_type : json
     * selected_status : 2
     */

    @SerializedName("id")
    private long channelId;

    @SerializedName("name")
    private String channelName;

    @SerializedName("type")
    private String channelType;

    @SerializedName("content_query_id")
    private String contentQueryId;

    @SerializedName("content_type")
    private String contentType;

    @SerializedName("selected_status")
    private int selectedStatus;

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @ChannelType
    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(@ChannelType String channelType) {
        this.channelType = channelType;
    }

    public String getContentQueryId() {
        return contentQueryId;
    }

    public void setContentQueryId(String contentQueryId) {
        this.contentQueryId = contentQueryId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getSelectedStatus() {
        return selectedStatus;
    }

    public void setSelectedStatus(int selectedStatus) {
        this.selectedStatus = selectedStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Channel) {
            Channel channel = (Channel) obj;
            if (channel.getChannelId() == channelId) {
                return (channelType == null && channelType.equals(channel.getChannelType()))
                        || channel.getChannelType().equals(channelType);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 12;
        result = result * 31 + (int) (channelId ^ (channelId >>> 32));
        if (channelType != null) {
            result = result * 31 + channelType.hashCode();
        }
        return result;
    }

}
