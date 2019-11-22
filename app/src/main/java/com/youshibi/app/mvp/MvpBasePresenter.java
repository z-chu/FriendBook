package com.youshibi.app.mvp;

import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.youshibi.app.AppException;

import java.lang.ref.WeakReference;


/**
 * Created by zchu on 16-11-17.
 */
public class MvpBasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private WeakReference<V> viewRef;

    /**
     * Presenter与View建立连接
     */
    @UiThread
    @Override
    public void attachView(V view) {
        viewRef = new WeakReference<>(view);
    }

    @UiThread
    @Override
    public void start() {
    }

    @UiThread
    @Nullable
    public V getView() {
        return viewRef == null ? null : viewRef.get();
    }


    /**
     * 每次调用业务请求的时候 即：getView().showXxx();时
     * 请先调用方法检查是否与View建立连接，没有则可能会空指针异常
     */
    @UiThread
    public final boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    /**
     * Presenter与View连接断开
     */
    @UiThread
    @Override
    public void detachView() {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }

    }

    @UiThread
    @Override
    public void destroy() {
        if (isViewAttached()) {
            detachView();
        }
    }

    protected String handleException(Throwable throwable) {
        return AppException.getExceptionMessage(throwable);
    }
}
