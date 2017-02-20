package com.youshibi.app.presentation.home;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youshibi.app.R;
import com.youshibi.app.data.bean.BookRt;
import com.youshibi.app.data.bean.DataList;
import com.youshibi.app.data.net.RequestClient;
import com.youshibi.app.data.net.RequestSubscriber;
import com.youshibi.app.base.BaseRxPresenter;
import com.youshibi.app.presentation.home.vo.BookItem;
import com.youshibi.app.ui.help.CommonAdapter;
import com.youshibi.app.ui.help.CommonViewHolder;
import com.youshibi.app.ui.widget.RatioImageView;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chu on 2016/12/3.
 */

public class HomePresenter extends BaseRxPresenter<HomeView> {
    private static final int PAGE_SIZE = 20;
    private int page;
    private int count;
    private CommonAdapter<BookItem> adapter;

    public void doLoadData(final boolean isRefresh) {
        page = 1;
        if (isViewAttached()) {
            getView().showLoading(isRefresh);
        }
        RequestClient.getServerAPI()
                .getBookList(page, PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestSubscriber<DataList<BookRt>>() {
                    @Override
                    public void onSuccess(DataList<BookRt> data) {
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

                    @Override
                    public void onResultError(int code, String msg) {
                        if (isViewAttached()) {
                            getView().showError(msg, isRefresh);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (isViewAttached()) {
                            getView().showError("网络出错", isRefresh);
                        }
                    }
                });
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
        RequestClient.getServerAPI()
                .getBookList(page, PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestSubscriber<DataList<BookRt>>() {
                    @Override
                    public void onSuccess(DataList<BookRt> data) {
                        if (isViewAttached()) {
                            adapter.addData(convertData(data.DataList));
                            getView().showMoreFrom();
                        }
                    }

                    @Override
                    public void onResultError(int code, String msg) {
                        if (isViewAttached()) {
                            getView().showMoreError();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (isViewAttached()) {
                            getView().showMoreError();
                        }
                    }
                });
    }

    private List<BookItem> convertData(List<BookRt> bookRts) {
        List<BookItem> bookItems = new ArrayList<>();
        for (BookRt bookRt : bookRts) {
            BookItem bookItem = new BookItem();
            bookItem.id = bookRt.BookId;
            bookItem.coverUrl = bookRt.BookImg;
            bookItem.title = bookRt.BookName;
            bookItem.describe = bookRt.BookIntro;
            bookItem.author = bookRt.BookAuthor;
            bookItem.isFinish = bookRt.IsFinish;
            bookItems.add(bookItem);
        }
        return bookItems;
    }

    private BaseQuickAdapter createAdapter(List<BookItem> bookItems) {
        adapter = new CommonAdapter<BookItem>(R.layout.list_item_book, bookItems) {
            @Override
            protected void convert(final CommonViewHolder holder, final BookItem bookItem) {
                RatioImageView ivCover = holder.getView(R.id.iv_cover);
                Glide
                        .with(mContext)
                        .load(bookItem.coverUrl)
                        .into(ivCover);

                holder
                        .setText(R.id.tv_author, bookItem.author)
                        .setText(R.id.tv_describe, bookItem.describe)
                        .setText(R.id.tv_title, bookItem.title);



            }

        };

        return adapter;
    }
}
