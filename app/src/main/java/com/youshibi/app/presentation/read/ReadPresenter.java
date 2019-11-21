package com.youshibi.app.presentation.read;

import android.support.annotation.NonNull;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youshibi.app.base.BaseRxPresenter;
import com.youshibi.app.data.DBManger;
import com.youshibi.app.data.DataManager;
import com.youshibi.app.data.bean.BookChapter;
import com.youshibi.app.data.bean.BookChapterContent;
import com.youshibi.app.data.db.table.BookTb;
import com.youshibi.app.event.BookcaseRefreshEvent;
import com.youshibi.app.rx.ProgressSubscriber;
import com.youshibi.app.rx.RetryWithDelay;
import com.youshibi.app.rx.RxBus;
import com.youshibi.app.rx.SimpleSubscriber;

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
    private BookSectionAdapter bookSectionAdapter;
    private List<BookChapter> mBookSectionItems;

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
                .getBookSectionList(mBookId, true, null, null, mBookTb.getHasUpdate())
                .retryWhen(new RetryWithDelay(3, 3000))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<List<BookChapter>>() {
                    @Override
                    public void onNext(List<BookChapter> bookSectionItems) {

                        if (DBManger.getInstance().hasBookTb(mBookTb.getId())) {
                            mBookTb.setLatestReadTimestamp(System.currentTimeMillis()); //更新最后一次的阅读时间
                            mBookTb.setReadNumber(mBookTb.getReadNumber() + 1); //更新阅读次数
                            if (mBookTb.getHasUpdate()) {
                                mBookTb.setHasUpdate(false);
                            }
                            DBManger.getInstance().updateBookTb(mBookTb);
                            RxBus.getDefault().post(new BookcaseRefreshEvent());
                        }
                        if (isViewAttached()) {
                            getView().setSectionListAdapter(createBookSectionAdapter(bookSectionItems));
                        }
                        mBookSectionItems = bookSectionItems;
                        if (mSectionIndex != null && mSectionId != null) {
                            doLoadData(mSectionIndex, mSectionId, true);
                        } else {
                            BookChapter bookSectionItem = bookSectionItems.get(0);
                            doLoadData(bookSectionItem.getChapterIndex(), bookSectionItem.getChapterId(), true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (isViewAttached()) {
                            getView().showToast(handleException(e));
                        }
                    }
                });

    }

    @Override
    public void loadData() {
        getView().showLoading();
        loadSectionList();


    }

    @Override
    public void saveReadLocation() {

        mBookTb.setLatestReadSection(mSectionIndex);
        mBookTb.setLatestReadSectionId(mSectionId);
        DBManger.getInstance().updateBookTb(mBookTb);
    }

    /**
     * 找到sectionIndex对应的章节在bookSectionItems中的下标
     */
    private int indexOfSectionList(@NonNull List<BookChapter> bookSectionItems, String sectionId) {
        for (int i = 0; i < bookSectionItems.size(); i++) {
            if (bookSectionItems.get(i).getChapterId().equals(sectionId)) {
                return i;
            }
        }
        return -1;
    }

    private void loadSectionContent(final int sectionIndex, String sectionId, final boolean isOpen) {
        Subscription subscribe = DataManager
                .getInstance()
                .getBookSectionContent(mBookId, sectionId, sectionIndex)
                .retryWhen(new RetryWithDelay(3, 3000))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<BookChapterContent>(getView().provideContext()) {
                    protected void showProgressDialog() {
                        if (isOpen) {
                            super.showProgressDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (isViewAttached()) {
                            getView().showError(handleException(e));
                        }
                    }

                    @Override
                    public void onNext(BookChapterContent bookSectionContent) {
                        if (isViewAttached()) {
                            int listIndex = indexOfSectionList(mBookSectionItems, bookSectionContent.getChapterId());
                            if (mReadAdapter == null) {
                                mReadAdapter = new ReadAdapter();
                                mReadAdapter.addData(listIndex, bookSectionContent);
                                getView().setPageAdapter(mReadAdapter);

                            } else {
                                mReadAdapter.addData(listIndex, bookSectionContent);
                            }
                            if (isOpen) {
                                getView().openSection(listIndex, mBookTb.getLatestReadSectionId() == mSectionId ? mBookTb.getLatestReadPage() : 0);
                                getView().setSectionDisplay(bookSectionContent.getChapterName(),listIndex);
                            }

                        }
                    }
                });
        addSubscription2Detach(subscribe);
    }


    private void doLoadData(int sectionIndex, String sectionId, boolean isOpen) {
        this.mSectionIndex = sectionIndex;
        this.mSectionId = sectionId;
        int indexOfSectionList = indexOfSectionList(mBookSectionItems, sectionId);
        if (isOpen) {
            if ((mReadAdapter == null || !mReadAdapter.hasSection(indexOfSectionList))) {
                loadSectionContent(sectionIndex, sectionId, isOpen);
            } else {
                if (mReadAdapter != null && isViewAttached()) {
                    getView().openSection(indexOfSectionList, 0);
                    getView().setSectionDisplay(mBookSectionItems.get(indexOfSectionList).getChapterName(),indexOfSectionList);
                }
            }
        }
        if (mBookSectionItems != null) {
            if (indexOfSectionList + 1 < mBookSectionItems.size()) {
                if (mReadAdapter == null
                        || !mReadAdapter.hasSection(indexOfSectionList + 1)) {
                    BookChapter bookSectionItem = mBookSectionItems.get(indexOfSectionList + 1);
                    loadSectionContent(bookSectionItem.getChapterIndex(), bookSectionItem.getChapterId(), false);
                }
            }
            if (indexOfSectionList - 1 >= 0) {
                if (mReadAdapter == null
                        || !mReadAdapter.hasSection(indexOfSectionList - 1)) {
                    BookChapter bookSectionItem = mBookSectionItems.get(indexOfSectionList - 1);
                    loadSectionContent(bookSectionItem.getChapterIndex(), bookSectionItem.getChapterId(), false);
                }
            }
        }
    }


    private boolean isFirstChapterChange = true;

    @Override
    public void onChapterChange(int pos) {
        if (!isFirstChapterChange) {
            BookChapter bookSectionItem = mBookSectionItems.get(pos);
            doLoadData(bookSectionItem.getChapterIndex(), bookSectionItem.getChapterId(), false);
            getView().setSectionDisplay(bookSectionItem.getChapterName(),pos);
        } else {
            isFirstChapterChange = false;
        }
        mBookTb.setLatestReadSection(mSectionIndex);
        mBookTb.setLatestReadSectionId(mSectionId);
        DBManger.getInstance().updateBookTb(mBookTb);
    }

    @Override
    public void onPageCountChange(int count) {

    }

    @Override
    public void onPageChange(int pos) {
        mBookTb.setLatestReadPage(pos);
        DBManger.getInstance().updateBookTb(mBookTb);
    }

    private BookSectionAdapter createBookSectionAdapter(List<BookChapter> bookSectionItems) {
        bookSectionAdapter = new BookSectionAdapter(bookSectionItems);
        bookSectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookChapter bookSectionItem = ((List<BookChapter>) adapter.getData()).get(position);
                doLoadData(bookSectionItem.getChapterIndex(), bookSectionItem.getChapterId(), true);
            }
        });
        return bookSectionAdapter;
    }

    @Override
    public void nextSection() {
        int indexOfSectionList = indexOfSectionList(mBookSectionItems, mSectionId);
        if (indexOfSectionList + 1 < mBookSectionItems.size()) {
            BookChapter bookSectionItem = mBookSectionItems.get(indexOfSectionList + 1);
            doLoadData(bookSectionItem.getChapterIndex(), bookSectionItem.getChapterId(), true);
        }

    }

    @Override
    public void openSection(int pos) {
        BookChapter bookSectionItem = mBookSectionItems.get(pos);
        doLoadData(bookSectionItem.getChapterIndex(), bookSectionItem.getChapterId(), true);
    }

    @Override
    public void prevSection() {
        int indexOfSectionList = indexOfSectionList(mBookSectionItems, mSectionId);
        if (indexOfSectionList - 1 >= 0) {
            BookChapter bookSectionItem = mBookSectionItems.get(indexOfSectionList - 1);
            doLoadData(bookSectionItem.getChapterIndex(), bookSectionItem.getChapterId(), true);
        }
    }


}
