package com.youshibi.app.presentation.main;

import android.support.v4.view.PagerAdapter;

import com.youshibi.app.mvp.MvpView;

/**
 * Created by Chu on 2016/12/3.
 */

public interface MainView extends MvpView {

    void setViewPage(PagerAdapter adapter);
}
