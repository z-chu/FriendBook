package com.youshibi.app.mvp;

import android.support.annotation.UiThread;

/**
 * Created by zchu on 16-11-17.
 *
 */

public interface MvpPresenter<V extends MvpView> {

    /**
     * 调用在attachView(V view)之后，该方法被调用后，说明绑定的View已经初始化完毕了，可以安全的使用getView()调用View的各个方法了
     * <br/>
     * <b>约定:</b>每个presenter在调其他业务方法之前，start()必须调用至少一次
     */
    @UiThread
    void start();

    @UiThread
    void attachView(V view);

    @UiThread
    void detachView();

    @UiThread
    void destroy();


}
