package com.youshibi.app.presentation.main;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.youshibi.app.R;
import com.youshibi.app.mvp.MvpActivity;

/**
 * Created by Chu on 2016/11/30.
 */

public class MainActivity extends MvpActivity<MainContract.Presenter> implements MainContract.View, OnTabSelectListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        getPresenter().initContentContainer(getSupportFragmentManager(),R.id.content_view);
        bottomBar.setOnTabSelectListener(this);
    }

    @NonNull
    @Override
    public MainContract.Presenter createPresenter() {
        return new MainPresenter();
    }


    @Override
    public void onTabSelected(@IdRes int tabId) {
        getPresenter().dispatchTabSelectedTabId(tabId);

    }

}
