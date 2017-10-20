package com.youshibi.app.presentation.explore;

import com.youshibi.app.data.bean.Channel;

/**
 * author : zchu
 * date   : 2017/10/20
 * desc   :
 */

public class OnChannelClickEvent {

    public OnChannelClickEvent(Channel channel) {
        this.channel = channel;
    }

    public Channel channel;
}
