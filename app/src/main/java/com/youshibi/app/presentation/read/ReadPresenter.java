package com.youshibi.app.presentation.read;

import com.youshibi.app.base.BaseRxPresenter;
import com.youshibi.app.data.DataManager;
import com.youshibi.app.data.bean.BookSectionContent;
import com.youshibi.app.rx.SimpleSubscriber;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chu on 2017/5/28.
 */

public class ReadPresenter extends BaseRxPresenter<ReadContract.View> implements ReadContract.Presenter {

    public String mBookId;
    public int mSectionIndex;
    public ReadAdapter mReadAdapter;

    public ReadPresenter(String bookId, int sectionIndex) {
        this.mBookId = bookId;
        this.mSectionIndex = sectionIndex;

    }

    @Override
    public void start() {
        super.start();

    }

    @Override
    public void loadData() {
        getView().showLoading();
        doLoadData(mSectionIndex);
        doLoadData(mSectionIndex + 1);
        if (mSectionIndex > 1) {
            doLoadData(mSectionIndex - 1);
        }
    }

    private void doLoadData(final int sectionIndex) {
        Subscription subscribe = DataManager
                .getInstance()
                .getBookSectionContent(mBookId, sectionIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<BookSectionContent>() {

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (isViewAttached()) {
                            getView().showError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(BookSectionContent bookSectionContent) {
                        if (isViewAttached()) {
                            if (mReadAdapter == null) {
                                mReadAdapter = new ReadAdapter();
                                mReadAdapter.addData(sectionIndex, bookSectionContent);
                                getView().setPageAdapter(mReadAdapter);
                                getView().openSection(sectionIndex);
                            } else {
                                mReadAdapter.addData(sectionIndex, bookSectionContent);
                            }

                        }
                    }
                });
        addSubscription2Detach(subscribe);
    }

    @Override
    public void onChapterChange(int pos) {
        this.mSectionIndex = pos;
        if (!mReadAdapter.hasSection(mSectionIndex + 1)) {
            doLoadData(mSectionIndex + 1);
        }
        if (mSectionIndex > 1) {
            if (!mReadAdapter.hasSection(mSectionIndex - 1)) {
                doLoadData(mSectionIndex - 1);
            }
        }

    }

    @Override
    public void onPageCountChange(int count) {

    }

    @Override
    public void onPageChange(int pos) {

    }
}
