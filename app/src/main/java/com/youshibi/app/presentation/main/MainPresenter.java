package com.youshibi.app.presentation.main;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.youshibi.app.mvp.MvpBasePresenter;
import com.youshibi.app.presentation.home.HomeFragment;
import com.youshibi.app.ui.help.BaseFragmentAdapter;

import java.util.ArrayList;

/**
 * Created by Chu on 2016/12/3.
 */

public class MainPresenter extends MvpBasePresenter<MainView> {

    private String[] mPageTitles = {"精品", "分类", "书架" };

    public void initViewPage(@NonNull FragmentManager fragmentManager) {
        if (isViewAttached()) {
            final BaseFragmentAdapter fragmentAdapter = new BaseFragmentAdapter(fragmentManager);
            ArrayList<Fragment> fragments = new ArrayList<>();
            fragments.add(HomeFragment.newInstance());
            fragments.add(HomeFragment.newInstance());
            fragments.add(HomeFragment.newInstance());
            fragmentAdapter.setFragmentPages(fragments);
            fragmentAdapter.setPageTitleArray(mPageTitles);
            getView().setViewPage(fragmentAdapter);

        }


    }
}
