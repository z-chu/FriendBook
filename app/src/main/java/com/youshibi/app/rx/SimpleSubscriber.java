package com.youshibi.app.rx;

import com.zchu.log.Logger;

import rx.Subscriber;

/**
 * Created by Chu on 2017/5/29.
 */

public abstract class SimpleSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Logger.e(e);

    }

}
