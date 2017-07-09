package com.youshibi.app.exception;

/**
 * author : zchu
 * date   : 2017/7/7
 * desc   : 网络未连接且继续做请求网络操作时抛出
 */

public class NetNotConnectedException extends RuntimeException {

    public NetNotConnectedException(){
        super("The network is not connected and can not access the network");
    }
}
