package com.youshibi.app.presentation.main;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.youshibi.app.R;
import com.youshibi.app.mvp.MvpActivity;
import com.youshibi.app.presentation.bookcase.BookcaseFragment;
import com.youshibi.app.util.ToastUtil;

/**
 * Created by Chu on 2016/11/30.
 */

public class MainActivity extends MvpActivity<MainContract.Presenter> implements MainContract.View,
        BottomNavigationView.OnNavigationItemSelectedListener, BookcaseFragment.OnBookCaseEditListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private BottomNavigationView bottomNavigation;
    @IdRes
    private int selectedTabId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        getPresenter().start();
        getPresenter().initContentContainer(getSupportFragmentManager(), R.id.content_view);
        if(savedInstanceState!=null) {
            savedInstanceState.getInt("selectedTabId", R.id.tab_bookcase);
        }else {
            getPresenter().dispatchTabSelectedTabId(R.id.tab_bookcase);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedTabId", selectedTabId);
    }


    @NonNull
    @Override
    public MainContract.Presenter createPresenter() {
        return new MainPresenter();
    }


    @Override
    protected boolean isEnableSlideFinish() {
        return false;
    }

    @Override
    protected boolean isCountingPage() {
        return false;
    }

    @Override
    public void switchBookcase(@IdRes int tabId) {
        selectedTabId = tabId;
    }

    @Override
    public void switchExplore(@IdRes int tabId) {
        selectedTabId = tabId;
    }

    @Override
    public void switchMine(@IdRes int tabId) {
        selectedTabId = tabId;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return getPresenter().dispatchTabSelectedTabId(item.getItemId());
    }


    @Override
    public ViewGroup getBottomGroup() {
        return bottomNavigation;
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.showToast("再按一次退出");
                exitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }

        }
    }

}
