package com.youshibi.app.presentation.main;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.youshibi.app.mvp.MvpPresenter;
import com.youshibi.app.mvp.MvpView;

/**
 * Created by Chu on 2017/5/28.
 */

public interface MainContract {
    interface View extends MvpView {


    }

    interface Presenter extends MvpPresenter<View> {

        void initViewPage(@NonNull final FragmentManager fragmentManager);

        void dispatchTabSelectedTabId(@IdRes int tabId);
    }


}
