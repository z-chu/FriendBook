package com.youshibi.app.data.net;

import com.youshibi.app.data.bean.HttpResult;
import com.zchu.log.Logger;

import rx.Subscriber;

/**
 * @Description 重写RxJava的订阅者
 * @Author z.chu
 * @Date 2016/7/24.
 */
public abstract class RequestSubscriber<M> extends Subscriber<HttpResult<M>> {


    @Override
    public void onCompleted() {
        // do nothing by default
    }

    /**
     * 出现运行时异常,默认只打印异常信息
     */
    @Override
    public void onError(Throwable e) {
        Logger.e(e);
    }

    /**
     * 不要再在该方法处理结果，更不要去复写该方法
     * @param httpResult
     */
    @Deprecated
    @Override
    public void onNext(HttpResult<M> httpResult) {
        if (httpResult.isSuccessful()) {
            onSuccess(httpResult.data);
        } else {
            onResultError(httpResult.code, httpResult.message);
        }
    }

    /**
     * 请求完成
     *
     * @param data 实体类泛型
     */
    public abstract void onSuccess(M data);

    /**
     * 请求结果不正确
     *
     * @param code 错误码
     * @param msg  提示信息
     */
    public abstract void onResultError(int code, String msg);



}
