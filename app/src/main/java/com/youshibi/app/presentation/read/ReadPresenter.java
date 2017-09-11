package com.youshibi.app.presentation.read;

import android.view.View;

import com.youshibi.app.R;
import com.youshibi.app.base.BaseRxPresenter;
import com.youshibi.app.data.DBManger;
import com.youshibi.app.data.DataManager;
import com.youshibi.app.data.bean.BookSectionContent;
import com.youshibi.app.data.bean.BookSectionItem;
import com.youshibi.app.data.db.table.BookTb;
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
    private BookTb mBookTb;
    private Integer mSectionIndex;
    private String mSectionId;
    private ReadAdapter mReadAdapter;
    private CommonAdapter<BookSectionItem> bookSectionAdapter;
    private List<BookSectionItem> mBookSectionItems;

    private int mSectionListIndex = -1;

    public ReadPresenter(BookTb bookTb) {
        this.mBookTb = bookTb;
        this.mBookId = bookTb.getId();
        this.mSectionIndex = bookTb.getLatestReadSection();
        this.mSectionId = bookTb.getLatestReadSectionId();
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
                        }
                        mBookSectionItems = bookSectionItems;
                        if (mSectionIndex == null) {
                            mSectionIndex = bookSectionItems.get(0).getSectionIndex();
                            doLoadData(mSectionIndex);
                        } else {
                            for (int i = 0; i < mBookSectionItems.size(); i++) {
                                if (mBookSectionItems.get(i).getSectionIndex() == mSectionIndex) {
                                    mSectionListIndex = i;
                                    break;
                                }
                            }
                            if (mReadAdapter == null ||
                                    (mSectionListIndex + 1 < mBookSectionItems.size() && !mReadAdapter.hasSection(mBookSectionItems.get(mSectionListIndex + 1).getSectionIndex()))) {
                                doLoadDataNext(mSectionIndex);
                            }
                            if (mReadAdapter == null ||
                                    (mSectionListIndex - 1 >= 0 && !mReadAdapter.hasSection(mBookSectionItems.get(mSectionListIndex - 1).getSectionIndex()))) {
                                doLoadDataPrev(mSectionIndex);
                            }
                        }


                    }
                });

    }

    @Override
    public void loadData() {
        getView().showLoading();
        loadSectionList();
        if (mSectionIndex != null) {
            doLoadDataCurrent(mSectionIndex);
        }

    }

    private void doLoadData(int sectionIndex) {
        this.mSectionIndex = sectionIndex;
        if ((mReadAdapter == null || !mReadAdapter.hasSection(sectionIndex))) {
            doLoadDataCurrent(sectionIndex);
        } else {
            if (mReadAdapter != null) {
                getView().openSection(sectionIndex);
            }
        }
        if (mBookSectionItems != null) {
            for (int i = 0; i < mBookSectionItems.size(); i++) {
                if (mBookSectionItems.get(i).getSectionIndex() == mSectionIndex) {
                    mSectionListIndex = i;
                    break;
                }
            }
            if (mReadAdapter == null ||
                    (mSectionListIndex + 1 < mBookSectionItems.size() && !mReadAdapter.hasSection(mBookSectionItems.get(mSectionListIndex + 1).getSectionIndex()))) {
                doLoadDataNext(mSectionIndex);
            }
            if (mReadAdapter == null ||
                    (mSectionListIndex - 1 >= 0 && !mReadAdapter.hasSection(mBookSectionItems.get(mSectionListIndex - 1).getSectionIndex()))) {
                doLoadDataPrev(mSectionIndex);
            }
        }

    }

    private void doLoadDataCurrent(final int sectionIndex) {
        Subscription subscribe = DataManager
                .getInstance()
                .getBookSectionContent(mBookId, mSectionId, sectionIndex)
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
                                mReadAdapter.addData(bookSectionContent.getSectionIndex(), bookSectionContent);
                                getView().setPageAdapter(mReadAdapter);

                            } else {
                                mReadAdapter.addData(bookSectionContent.getSectionIndex(), bookSectionContent);
                            }
                            getView().openSection(bookSectionContent.getSectionIndex());

                        }
                    }
                });
        addSubscription2Detach(subscribe);
    }

    private void doLoadDataPrev(final int sectionIndex) {
        Subscription subscribe = DataManager
                .getInstance()
                .getBookSectionContentPrev(mBookId, mSectionId, sectionIndex)
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
                                mReadAdapter.addData(bookSectionContent.getSectionIndex(), bookSectionContent);
                                getView().setPageAdapter(mReadAdapter);
                            } else {
                                mReadAdapter.addData(bookSectionContent.getSectionIndex(), bookSectionContent);
                            }

                        }
                    }
                });
        addSubscription2Detach(subscribe);
    }

    private void doLoadDataNext(final int sectionIndex) {
        Subscription subscribe = DataManager
                .getInstance()
                .getBookSectionContentNext(mBookId, mSectionId, sectionIndex)
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
                                mReadAdapter.addData(bookSectionContent.getSectionIndex(), bookSectionContent);
                                getView().setPageAdapter(mReadAdapter);
                            } else {
                                mReadAdapter.addData(bookSectionContent.getSectionIndex(), bookSectionContent);
                            }


                        }
                    }
                });
        addSubscription2Detach(subscribe);
    }


    @Override
    public void onChapterChange(int pos) {
        doLoadData(pos);

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
                        doLoadData(item.getSectionIndex());
                    }
                });
            }
        };
        return bookSectionAdapter;
    }

    @Override
    public void detachView() {
        super.detachView();
        mBookTb.setLatestReadTimestamp(System.currentTimeMillis()); //更新最后一次的阅读时间
        mBookTb.setReadNumber(mBookTb.getReadNumber() + 1); //更新阅读次数
        mBookTb.setLatestReadSection(mSectionIndex);
        DBManger.getInstance().updateBookTb(mBookTb);
    }
}
