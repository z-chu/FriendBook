package com.youshibi.app.presentation.read;

import android.support.annotation.NonNull;
import android.view.View;

import com.youshibi.app.R;
import com.youshibi.app.base.BaseRxPresenter;
import com.youshibi.app.data.DBManger;
import com.youshibi.app.data.DataManager;
import com.youshibi.app.data.bean.BookSectionContent;
import com.youshibi.app.data.bean.BookSectionItem;
import com.youshibi.app.data.db.table.BookTb;
import com.youshibi.app.event.BookcaseRefreshEvent;
import com.youshibi.app.rx.RetryWithDelay;
import com.youshibi.app.rx.RxBus;
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
                .retryWhen(new RetryWithDelay(3,3000))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<List<BookSectionItem>>() {
                    @Override
                    public void onNext(List<BookSectionItem> bookSectionItems) {

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
                            BookSectionItem bookSectionItem = bookSectionItems.get(0);
                            doLoadData(bookSectionItem.getSectionIndex(), bookSectionItem.getSectionId(), true);
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
    private int indexOfSectionList(@NonNull List<BookSectionItem> bookSectionItems, String sectionId) {
        for (int i = 0; i < bookSectionItems.size(); i++) {
            if (bookSectionItems.get(i).getSectionId().equals(sectionId)) {
                return i;
            }
        }
        return -1;
    }

    private void loadSectionContent(final int sectionIndex, String sectionId, final boolean isOpen) {
        Subscription subscribe = DataManager
                .getInstance()
                .getBookSectionContent(mBookId, sectionId, sectionIndex)
                .retryWhen(new RetryWithDelay(3,3000))
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
                            int listIndex = indexOfSectionList(mBookSectionItems, bookSectionContent.getSectionId());
                            if (mReadAdapter == null) {
                                mReadAdapter = new ReadAdapter();
                                mReadAdapter.addData(listIndex, bookSectionContent);
                                getView().setPageAdapter(mReadAdapter);

                            } else {
                                mReadAdapter.addData(listIndex, bookSectionContent);
                            }
                            if (isOpen) {
                                getView().openSection(listIndex, mBookTb.getLatestReadSectionId() == mSectionId ? mBookTb.getLatestReadPage() : 0);
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
                }
            }
        }
        if (mBookSectionItems != null) {
            if (indexOfSectionList + 1 < mBookSectionItems.size()) {
                if (mReadAdapter == null
                        || !mReadAdapter.hasSection(indexOfSectionList + 1)) {
                    BookSectionItem bookSectionItem = mBookSectionItems.get(indexOfSectionList + 1);
                    loadSectionContent(bookSectionItem.getSectionIndex(), bookSectionItem.getSectionId(), false);
                }
            }
            if (indexOfSectionList - 1 >= 0) {
                if (mReadAdapter == null
                        || !mReadAdapter.hasSection(indexOfSectionList - 1)) {
                    BookSectionItem bookSectionItem = mBookSectionItems.get(indexOfSectionList - 1);
                    loadSectionContent(bookSectionItem.getSectionIndex(), bookSectionItem.getSectionId(), false);
                }
            }
        }
    }


    private boolean isFirstChapterChange = true;

    @Override
    public void onChapterChange(int pos) {
        if (!isFirstChapterChange) {
            BookSectionItem bookSectionItem = mBookSectionItems.get(pos);
            doLoadData(bookSectionItem.getSectionIndex(), bookSectionItem.getSectionId(), false);
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

    private CommonAdapter<BookSectionItem> createBookSectionAdapter(List<BookSectionItem> bookSectionItems) {
        bookSectionAdapter = new CommonAdapter<BookSectionItem>(R.layout.list_item_book_section, bookSectionItems) {
            @Override
            protected void convert(CommonViewHolder helper, final BookSectionItem item) {
                helper.setText(R.id.tv_section_name, item.getSectionName());
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doLoadData(item.getSectionIndex(), item.getSectionId(), true);
                    }
                });
            }
        };
        return bookSectionAdapter;
    }

}
