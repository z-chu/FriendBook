package com.youshibi.app.mvp;

import android.support.annotation.UiThread;

/**
 * Created by zchu on 16-11-17.
 *
 */

public interface MvpPresenter<V extends MvpView> {


    @UiThread
    void attachView(V view);


    @UiThread
    void detachView();


}
