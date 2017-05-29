package com.youshibi.app.presentation.book;

import android.view.View;

import com.youshibi.app.AppNavigator;
import com.youshibi.app.R;
import com.youshibi.app.data.DataManager;
import com.youshibi.app.data.bean.BookSectionItem;
import com.youshibi.app.mvp.MvpBasePresenter;
import com.youshibi.app.rx.SimpleSubscriber;
import com.youshibi.app.ui.help.CommonAdapter;
import com.youshibi.app.ui.help.CommonViewHolder;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chu on 2017/5/28.
 */

public class BookDetailPresenter extends MvpBasePresenter<BookDetailContract.View> implements BookDetailContract.Presenter {

    private String bookId;

    public BookDetailPresenter(String bookId) {
        this.bookId = bookId;
    }



    @Override
    public void loadData() {
        getView().showLoading();
        DataManager
                .getInstance()
                .getBookSectionList(bookId, true)
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

    }

    private CommonAdapter<BookSectionItem> createBookSectionAdapter(List<BookSectionItem> bookSectionItems) {
        return new CommonAdapter<BookSectionItem>(R.layout.list_item_book_section, bookSectionItems) {
            @Override
            protected void convert(CommonViewHolder helper, final BookSectionItem item) {
                helper.setText(R.id.tv_section_name, item.getSectionName());
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppNavigator.showReadActivity(mContext, bookId, item.getSectionIndex());
                    }
                });
            }
        };
    }
}
