package com.youshibi.app.presentation.book;

import android.content.Context;
import android.view.View;

import com.youshibi.app.AppRouter;
import com.youshibi.app.R;
import com.youshibi.app.base.BaseRxPresenter;
import com.youshibi.app.data.DataManager;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.data.bean.BookChapter;
import com.youshibi.app.data.bean.BookDetail;
import com.youshibi.app.data.bean.DataList;
import com.youshibi.app.rx.SimpleSubscriber;
import com.youshibi.app.ui.help.CommonAdapter;
import com.youshibi.app.ui.help.CommonViewHolder;

import java.util.List;
import java.util.Random;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chu on 2017/5/28.
 */

public class BookDetailPresenter extends BaseRxPresenter<BookDetailContract.View> implements BookDetailContract.Presenter {

    private Book book;
    private CommonAdapter<BookChapter> bookSectionAdapter;

    public BookDetailPresenter(Book book) {
        this.book = book;
    }


    @Override
    public void loadData() {
        if (isViewAttached()) {
            if (bookSectionAdapter != null) {
                getView().setListAdapter(bookSectionAdapter);
                getView().showContent();
                return;
            }
            getView().showLoading();
        }

        DataManager
                .getInstance()
                .getBookDetail(book.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<BookDetail>() {
                    @Override
                    public void onNext(BookDetail bookDetail) {
                        if (isViewAttached()) {
                            getView().setData(bookDetail);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (isViewAttached()) {
                            getView().showError(e.getMessage());
                        }
                    }
                });

        loadRecommendBooks();
    }

    private void loadRecommendBooks() {
        Subscription subscribe = DataManager
                .getInstance()
                .getBookList(book.getBookTypeId(), new Random().nextInt(10)+1, 6)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<DataList<Book>>() {
                    @Override
                    public void onNext(DataList<Book> bookDataList) {
                        if (isViewAttached()) {
                            getView().setRecommendBooks(bookDataList.data_list);
                        }
                    }
                });
        addSubscription2Destroy(subscribe);
    }

    private void loadSectionList() {
        Subscription subscribe = DataManager
                .getInstance()
                .getBookSectionList(book.getId(), true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<List<BookChapter>>() {


                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (isViewAttached()) {
                            getView().showError(e.getMessage());
                        }

                    }

                    @Override
                    public void onNext(List<BookChapter> bookSectionItems) {
                        if (isViewAttached()) {
                            getView().setListAdapter(createBookSectionAdapter(bookSectionItems));
                            getView().showContent();
                        }

                    }
                });
        addSubscription2Detach(subscribe);
    }

    @Override
    public void openRead(Context context) {
        if (bookSectionAdapter != null && bookSectionAdapter.getData().size() > 0) {
            BookChapter bookSectionItem = bookSectionAdapter.getData().get(0);
            AppRouter.showReadActivity(context, book, bookSectionItem.getChapterIndex(), bookSectionItem.getChapterId());
        } else {
            AppRouter.showReadActivity(context, book, null, null);
        }
    }

    private CommonAdapter<BookChapter> createBookSectionAdapter(List<BookChapter> bookSectionItems) {
        bookSectionAdapter = new CommonAdapter<BookChapter>(R.layout.list_item_book_section, bookSectionItems) {
            @Override
            protected void convert(CommonViewHolder helper, final BookChapter item) {
                helper.setText(R.id.tv_section_name, item.getChapterName());
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppRouter.showReadActivity(v.getContext(), book, item.getChapterIndex(), item.getChapterId());
                    }
                });
            }
        };
        return bookSectionAdapter;
    }
}
