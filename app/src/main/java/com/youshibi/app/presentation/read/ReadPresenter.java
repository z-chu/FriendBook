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

    public String bookId;
    public int sectionIndex;

    public ReadPresenter(String bookId, int sectionIndex) {
        this.bookId = bookId;
        this.sectionIndex = sectionIndex;

    }

    @Override
    public void start() {
        super.start();

    }

    @Override
    public void loadData() {
        getView().showLoading();
        Subscription subscribe = DataManager
                .getInstance()
                .getBookSectionContent(bookId, sectionIndex)
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
                            getView().setData(bookSectionContent);
                            getView().showContent();
                        }
                    }
                });
        addSubscription2Detach(subscribe);
    }
}
