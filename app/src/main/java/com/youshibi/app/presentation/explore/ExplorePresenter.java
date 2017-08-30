package com.youshibi.app.presentation.explore;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.youshibi.app.base.BaseRxPresenter;
import com.youshibi.app.data.DataManager;
import com.youshibi.app.data.bean.BookType;
import com.youshibi.app.data.prefs.PreferencesHelper;
import com.youshibi.app.presentation.book.BookFragment;
import com.youshibi.app.rx.RxBus;
import com.youshibi.app.rx.SimpleSubscriber;
import com.zchu.rxcache.data.CacheResult;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chu on 2017/8/26.
 */

public class ExplorePresenter extends BaseRxPresenter<ExploreContract.View> implements ExploreContract.Presenter {

    private List<BookType> mBookTypes;
    private ArrayList<BookType> mAlwaysSelectedBookLabels;
    private ArrayList<BookType> mCommonSelectedBookLabels;
    private ArrayList<BookType> mUnselectedBookLabels;


    @Override
    public void loadData() {
        getView().showLoading();
        Subscription subscribe = DataManager
                .getInstance()
                .getBookType()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<CacheResult<List<BookType>>>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (isViewAttached()) {
                            getView().showError(handleException(e));
                        }
                    }

                    @Override
                    public void onNext(CacheResult<List<BookType>> cacheResult) {
                        List<BookType> data = cacheResult.getData();
                        if (data != null && data.size() > 0) {
                            processData(data, PreferencesHelper.getInstance().getSelectedBookLabels());
                        }
                    }
                });
        addSubscription2Destroy(subscribe);
    }

   /* private void processCacheData(@NonNull List<BookType> bookTypes, @Nullable List<BookType> selectedBookLabels) {
        mBookTypes = bookTypes;
        if (selectedBookLabels == null) {
            selectedBookLabels = new ArrayList<>();
            for (BookType bookType : bookTypes) {
                if (!bookType.isUnselected()) {
                    selectedBookLabels.add(bookType);
                }
            }
        }
        Fragment[] fragments = null;
        String[] pageTitles = null;
        if (selectedBookLabels.size() > 0) {
            fragments = new Fragment[bookTypes.size()];
            pageTitles = new String[bookTypes.size()];
            for (int i = 0; i < selectedBookLabels.size(); i++) {
                BookType bookType = bookTypes.get(i);
                fragments[i] = BookFragment.newInstance(bookType.getId());
                pageTitles[i] = bookType.getTypeName();
            }
        }
        if (isViewAttached()) {
            if (fragments != null) {
                getView().setTabContent(fragments, pageTitles);
            }
            getView().showContent();
        }
    }*/

    private void processData(@NonNull List<BookType> bookTypes, @Nullable List<BookType> selectedBookLabels) {
        if (mBookTypes == null) {
            mBookTypes = bookTypes;
            boolean isFirst =( selectedBookLabels == null || selectedBookLabels.size() == 0);
            if (isFirst && selectedBookLabels == null) {
                selectedBookLabels = new ArrayList<>();
            }
            if (!isFirst) {
                for (int i = selectedBookLabels.size() - 1; i >= 0; i--) {
                    if (selectedBookLabels.get(i).getSelectedStatus() == BookType.STATUS_ALWAYS_SELECTED) {
                        selectedBookLabels.remove(i);
                    }
                }
                mCommonSelectedBookLabels = new ArrayList<>(selectedBookLabels);
            }else{
                mCommonSelectedBookLabels=new ArrayList<>();
            }
            mAlwaysSelectedBookLabels = new ArrayList<>();
            mUnselectedBookLabels = new ArrayList<>();
            for (BookType bookType : bookTypes) {
             /*   if (isFirst) {
                    if (!bookType.isUnselected()) {
                        selectedBookLabels.add(bookType);
                    }
                }*/
                if (bookType.getSelectedStatus() == BookType.STATUS_ALWAYS_SELECTED) {
                    mAlwaysSelectedBookLabels.add(bookType);
                }else{

                    if(isFirst&&bookType.getSelectedStatus() == BookType.STATUS_DEFAULT_SELECTED){
                        mCommonSelectedBookLabels.add(bookType);
                        selectedBookLabels.add(bookType);
                        continue;
                    }
                    if(!selectedBookLabels.contains(bookType)){
                        mUnselectedBookLabels.add(bookType);

                    }
                }
            }
           // if (!isFirst) {
                selectedBookLabels.addAll(0, mAlwaysSelectedBookLabels);
           // }
            Fragment[] fragments = null;
            String[] pageTitles = null;
            if (selectedBookLabels.size() > 0) {
                fragments = new Fragment[selectedBookLabels.size()];
                pageTitles = new String[selectedBookLabels.size()];
                for (int i = 0; i < selectedBookLabels.size(); i++) {
                    BookType bookType = bookTypes.get(i);
                    fragments[i] = BookFragment.newInstance(bookType.getId());
                    pageTitles[i] = bookType.getTypeName();
                }
            }
            if (isViewAttached()) {
                if (fragments != null) {
                    getView().setTabContent(fragments, pageTitles);
                }
                getView().showContent();
                subscribeEvent();
            }
        } else {
            mBookTypes = bookTypes;
        }


    }

    private void subscribeEvent() {
        Subscription subscribe = RxBus
                .getDefault()
                .toObservable(OnSelectionEditFinishEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<OnSelectionEditFinishEvent>() {
                    @Override
                    public void onNext(OnSelectionEditFinishEvent onSelectionEditFinishEvent) {

                        mCommonSelectedBookLabels = onSelectionEditFinishEvent.selectedLabels;
                        mUnselectedBookLabels = onSelectionEditFinishEvent.unselectedLabel;
                        ArrayList<BookType> selectedBookLabels = new ArrayList<>(mCommonSelectedBookLabels);
                        selectedBookLabels.addAll(0, mAlwaysSelectedBookLabels);
                        PreferencesHelper.getInstance().setSelectedBookLabels(selectedBookLabels);
                        Fragment[] fragments = null;
                        String[] pageTitles = null;
                        if (selectedBookLabels.size() > 0) {
                            fragments = new Fragment[selectedBookLabels.size()];
                            pageTitles = new String[selectedBookLabels.size()];
                            for (int i = 0; i < selectedBookLabels.size(); i++) {
                                BookType bookType = selectedBookLabels.get(i);
                                fragments[i] = BookFragment.newInstance(bookType.getId());
                                pageTitles[i] = bookType.getTypeName();
                            }
                        } else {
                            fragments = new Fragment[0];
                            pageTitles = new String[0];
                        }
                        if (isViewAttached()) {
                            getView().setTabContent(fragments, pageTitles);
                        }
                    }
                });
        addSubscription2Destroy(subscribe);

    }

    @Override
    public void openBookTypeSelection(Context context) {
        Intent intent = BookTypeSelectionActivity
                .newIntent(context, mCommonSelectedBookLabels, mUnselectedBookLabels, mAlwaysSelectedBookLabels);
        context.startActivity(intent);
    }
}
