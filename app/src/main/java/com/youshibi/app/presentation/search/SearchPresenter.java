package com.youshibi.app.presentation.search;

import com.youshibi.app.AppException;
import com.youshibi.app.base.BaseRxPresenter;
import com.youshibi.app.data.DataManager;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.data.bean.DataList;
import com.youshibi.app.rx.SchedulersCompat;
import com.youshibi.app.rx.SimpleSubscriber;
import com.zchu.log.Logger;

/**
 * Created by Chu on 2017/6/11.
 */

public class SearchPresenter extends BaseRxPresenter<SearchContract.View> implements SearchContract.Presenter {

    @Override
    public void search(String keyword) {
        if (keyword == null || keyword.length() == 0) {
            return;
        }
        if (isViewAttached()) {
            getView().showLoading();
        }
        DataManager
                .getInstance()
                .searchBook(keyword, 1, 20)
                .compose(SchedulersCompat.<DataList<Book>>applyIoSchedulers())
                .subscribe(new SimpleSubscriber<DataList<Book>>() {
                    @Override
                    public void onNext(DataList<Book> bookDataList) {
                        Logger.e(bookDataList);
                        if (isViewAttached()) {
                            getView().showContent();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (isViewAttached()) {
                            getView().showError(AppException.getExceptionMessage(e));
                        }
                    }
                });
    }
}
