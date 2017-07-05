package com.youshibi.app.presentation.main;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.youshibi.app.R;
import com.youshibi.app.mvp.MvpBasePresenter;
import com.youshibi.app.presentation.bookcase.BookcaseFragment;
import com.youshibi.app.presentation.explore.ExploreFragment;
import com.youshibi.app.presentation.mine.MineFragment;

import java.util.List;

/**
 * Created by Chu on 2016/12/3.
 */

public class MainPresenter extends MvpBasePresenter<MainContract.View> implements MainContract.Presenter {

    private FragmentManager mFragmentManager;

    private int contentContainerId;

    @Override
    public void start() {
        super.start();

    }

    @Override
    public void initContentContainer(@NonNull final FragmentManager fragmentManager,@IdRes int contentContainerId) {
        this.mFragmentManager = fragmentManager;
        this.contentContainerId=contentContainerId;
    }




    @Override
    public void dispatchTabSelectedTabId(@IdRes int tabId) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        Fragment selectedFragment = mFragmentManager.findFragmentByTag(String.valueOf(tabId));
        switch (tabId){
            case R.id.tab_bookcase:
                if (selectedFragment== null) {
                    selectedFragment= BookcaseFragment.newInstance();
                    fragmentTransaction.add(contentContainerId, selectedFragment,String.valueOf(tabId));
                }
                break;
            case R.id.tab_explore:
                if (selectedFragment== null) {
                    selectedFragment= ExploreFragment.newInstance();
                    fragmentTransaction.add(contentContainerId, selectedFragment,String.valueOf(tabId));
                }
                break;
            case R.id.tab_mine:
                if (selectedFragment== null) {
                    selectedFragment= MineFragment.newInstance();
                    fragmentTransaction.add(contentContainerId, selectedFragment,String.valueOf(tabId));
                }
                break;
        }
        List<Fragment> fragments = mFragmentManager.getFragments();
        if(fragments!=null&&fragments.size()>0){
            for (Fragment fragment : fragments) {
                if(fragment.getTag().equals(String.valueOf(tabId))){
                    fragmentTransaction.show(fragment);
                }else{
                    fragmentTransaction.hide(fragment);
                }
            }
        }

        fragmentTransaction.commit();

    }

}
