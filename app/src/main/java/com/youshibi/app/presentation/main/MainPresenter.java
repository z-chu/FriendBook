package com.youshibi.app.presentation.main;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.youshibi.app.data.DataManager;
import com.youshibi.app.data.bean.BookType;
import com.youshibi.app.mvp.MvpBasePresenter;
import com.youshibi.app.presentation.home.HomeFragment;
import com.youshibi.app.ui.help.BaseFragmentAdapter;
import com.zchu.log.Logger;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chu on 2016/12/3.
 */

public class MainPresenter extends MvpBasePresenter<MainView> {


    public void initViewPage(@NonNull final FragmentManager fragmentManager) {
        DataManager
                .getInstance()
                .getBookType()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<BookType>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e);
                        if ((isViewAttached())) {
                            getView().showToast(e.getMessage());
                        }

                    }

                    @Override
                    public void onNext(ArrayList<BookType> bookTypes) {
                        if (isViewAttached()) {
                            final BaseFragmentAdapter fragmentAdapter = new BaseFragmentAdapter(fragmentManager);
                            Fragment[] fragments = new Fragment[bookTypes.size()+1];
                            fragments[0]=HomeFragment.newInstance();
                            String[] pageTitles = new String[bookTypes.size()+1];
                            pageTitles[0]="全部";
                            for (int i = 0; i < bookTypes.size(); i++) {
                                BookType bookType = bookTypes.get(i);
                                pageTitles[i+1] = bookType.getTypeName();
                                fragments[i+1] = HomeFragment.newInstance(bookType.getId());
                            }
                            fragmentAdapter.setFragmentPages(fragments);
                            fragmentAdapter.setPageTitles(pageTitles);
                            getView().setViewPage(fragmentAdapter);

                        }

                    }
                });


    }
}
