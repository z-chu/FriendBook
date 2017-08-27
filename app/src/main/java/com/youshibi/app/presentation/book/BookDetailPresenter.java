package com.youshibi.app.presentation.book;

import android.content.Context;
import android.view.View;

import com.youshibi.app.AppRouter;
import com.youshibi.app.R;
import com.youshibi.app.base.BaseRxPresenter;
import com.youshibi.app.data.DataManager;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.data.bean.BookSectionItem;
import com.youshibi.app.rx.SimpleSubscriber;
import com.youshibi.app.ui.help.CommonAdapter;
import com.youshibi.app.ui.help.CommonViewHolder;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chu on 2017/5/28.
 */

public class BookDetailPresenter extends BaseRxPresenter<BookDetailContract.View> implements BookDetailContract.Presenter {

    private Book book;
    private CommonAdapter<BookSectionItem> bookSectionAdapter;

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

        Subscription subscribe = DataManager
                .getInstance()
                .getBookSectionList(book.getId(), true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<List<BookSectionItem>>() {


                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (isViewAttached()) {
                            getView().showError(e.getMessage());
                        }

                    }

                    @Override
                    public void onNext(List<BookSectionItem> bookSectionItems) {
                        if (isViewAttached()) {
                            getView().setListAdapter(createBookSectionAdapter(bookSectionItems));
                            getView().showContent();
                        }

                    }
                });
        addSubscription2Detach(subscribe);

    }

    @Override
    public void openRead(Context context, int sectionIndex) {
        AppRouter.showReadActivity(context, book.getId(), this.book.getName(), sectionIndex);
    }

    private CommonAdapter<BookSectionItem> createBookSectionAdapter(List<BookSectionItem> bookSectionItems) {
        bookSectionAdapter = new CommonAdapter<BookSectionItem>(R.layout.list_item_book_section, bookSectionItems) {
            @Override
            protected void convert(CommonViewHolder helper, final BookSectionItem item) {
                helper.setText(R.id.tv_section_name, item.getSectionName());
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openRead(v.getContext(), item.getSectionIndex());
                    }
                });
            }
        };
        return bookSectionAdapter;
    }
}
