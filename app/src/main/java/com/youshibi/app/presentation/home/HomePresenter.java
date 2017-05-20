package com.youshibi.app.presentation.home;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youshibi.app.base.BaseRxPresenter;
import com.youshibi.app.data.DataManager;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.data.bean.DataList;
import com.youshibi.app.presentation.home.vo.BookItem;
import com.youshibi.app.ui.help.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chu on 2016/12/3.
 */

public class HomePresenter extends BaseRxPresenter<HomeView> {
    private static final int PAGE_SIZE = 20;
    private int page;
    private int count;
    private long bookType;
    private CommonAdapter<BookItem> adapter;

    public HomePresenter(long bookType){
        this.bookType=bookType;
    }

    public void doLoadData(final boolean isRefresh) {
        page = 1;
        if (isViewAttached()) {
            getView().showLoading(isRefresh);
        }
        Subscription subscription = DataManager
                .getInstance()
                .getBookList(page, PAGE_SIZE,bookType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DataList<Book>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().showError(e.getMessage(), isRefresh);
                        }
                    }

                    @Override
                    public void onNext(DataList<Book> data) {
                        count = data.Count;
                        if (isViewAttached()) {
                            if (!isRefresh) {
                                getView().setAdapter(createAdapter(convertData(data.DataList)));
                                getView().showContent();
                            } else {
                                adapter.setNewData(convertData(data.DataList));
                                getView().showContent();
                            }
                        }
                    }
                });
        addSubscription(subscription);
    }

    public void doLoadMoreData() {
        if (page * PAGE_SIZE >= count) {
            if (isViewAttached()) {
                getView().showTheEnd();
                return;
            }
        }
        page++;

        if (isViewAttached()) {
            getView().showMoreLoading();
        }
        Subscription subscription = DataManager.getInstance()
                .getBookList(page, PAGE_SIZE,bookType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DataList<Book>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().showMoreError();
                        }
                    }

                    @Override
                    public void onNext(DataList<Book> data) {
                        if (isViewAttached()) {
                            adapter.addData(convertData(data.DataList));
                            getView().showMoreFrom();
                        }
                    }
                });

        addSubscription(subscription);
    }

    private List<BookItem> convertData(List<Book> books) {
        List<BookItem> bookItems = new ArrayList<>();
        for (Book book : books) {
            BookItem bookItem = new BookItem();
            bookItem.id = book.BookId;
            bookItem.coverUrl = book.BookImg;
            bookItem.title = book.BookName;
            bookItem.describe = book.BookIntro;
            bookItem.author = book.BookAuthor;
            bookItem.isFinish = book.IsFinish;
            bookItems.add(bookItem);
        }
        return bookItems;
    }

    private BaseQuickAdapter createAdapter(List<BookItem> bookItems) {
        adapter = new BookAdapter(bookItems);
        return adapter;
    }
}
