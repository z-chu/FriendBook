package com.youshibi.app.presentation.read;

import android.view.View;

import com.youshibi.app.R;
import com.youshibi.app.base.BaseRxPresenter;
import com.youshibi.app.data.DataManager;
import com.youshibi.app.data.bean.BookSectionContent;
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

public class ReadPresenter extends BaseRxPresenter<ReadContract.View> implements ReadContract.Presenter {

    private String mBookId;
    private int mSectionIndex;
    private ReadAdapter mReadAdapter;
    private CommonAdapter<BookSectionItem> bookSectionAdapter;


    public ReadPresenter(String bookId, int sectionIndex) {
        this.mBookId = bookId;
        this.mSectionIndex = sectionIndex;

    }

    @Override
    public void start() {
        super.start();

    }

    private void loadSectionList() {
        DataManager
                .getInstance()
                .getBookSectionList(mBookId, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<List<BookSectionItem>>() {
                    @Override
                    public void onNext(List<BookSectionItem> bookSectionItems) {
                        if (isViewAttached()) {
                            getView().setSectionListAdapter(createBookSectionAdapter(bookSectionItems));
                            doLoadData(mSectionIndex, true);
                            doLoadData(mSectionIndex + 1, false);
                            if (mSectionIndex > 1) {
                                doLoadData(mSectionIndex - 1, false);
                            }
                        }
                    }
                });

    }

    @Override
    public void loadData() {
        getView().showLoading();
        loadSectionList();
    }

    private void doLoadData(final int sectionIndex, final boolean isOpen) {
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
                            getView().showError(handleException(e));
                        }
                    }

                    @Override
                    public void onNext(BookSectionContent bookSectionContent) {
                        if (isViewAttached()) {
                            if (mReadAdapter == null) {
                                mReadAdapter = new ReadAdapter();
                                mReadAdapter.addData(sectionIndex, bookSectionContent);
                                getView().setPageAdapter(mReadAdapter);

                            } else {
                                mReadAdapter.addData(sectionIndex, bookSectionContent);
                            }
                            if (isOpen) {
                                getView().openSection(sectionIndex);
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
            doLoadData(mSectionIndex + 1, false);
        }
        if (mSectionIndex > 1) {
            if (!mReadAdapter.hasSection(mSectionIndex - 1)) {
                doLoadData(mSectionIndex - 1, false);
            }
        }

    }

    @Override
    public void onPageCountChange(int count) {

    }

    @Override
    public void onPageChange(int pos) {

    }

    private CommonAdapter<BookSectionItem> createBookSectionAdapter(List<BookSectionItem> bookSectionItems) {
        bookSectionAdapter = new CommonAdapter<BookSectionItem>(R.layout.list_item_book_section, bookSectionItems) {
            @Override
            protected void convert(CommonViewHolder helper, final BookSectionItem item) {
                helper.setText(R.id.tv_section_name, item.getSectionName());
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doLoadData(item.getSectionIndex(), true);
                        doLoadData(item.getSectionIndex() + 1, false);
                        if (item.getSectionIndex() > 1) {
                            doLoadData(item.getSectionIndex() - 1, false);
                        }
                        //getView().showRead(bookId, item.getSectionIndex());
                    }
                });
            }
        };
        return bookSectionAdapter;
    }
}
