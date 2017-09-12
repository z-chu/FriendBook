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
                        if (mSectionIndex != null && mSectionId != null) {
                            doLoadData(mSectionIndex, mSectionId);
                        } else {
                            BookSectionItem bookSectionItem = bookSectionItems.get(0);
                            doLoadData(bookSectionItem.getSectionIndex(), bookSectionItem.getSectionId());
                        }
                    }
                });

    }

    @Override
    public void loadData() {
        getView().showLoading();
        loadSectionList();


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
                                getView().openSection(listIndex);
                            }

                        }
                    }
                });
        addSubscription2Detach(subscribe);
    }


    private void doLoadData(int sectionIndex, String sectionId) {
        this.mSectionIndex = sectionIndex;
        this.mSectionId = sectionId;
        if ((mReadAdapter == null || !mReadAdapter.hasSection(sectionIndex))) {
            //doLoadDataCurrent(sectionIndex);
            loadSectionContent(sectionIndex, sectionId, true);
        } else {
            if (mReadAdapter != null) {
                getView().openSection(sectionIndex);
            }
        }
        if (mBookSectionItems != null) {
            for (int i = 0; i < mBookSectionItems.size(); i++) {
                if (mBookSectionItems.get(i).getSectionIndex() == mSectionIndex) {
                    break;
                }
            }
            int indexOfSectionList = indexOfSectionList(mBookSectionItems, sectionId);
            if (indexOfSectionList + 1 < mBookSectionItems.size()) {
                if (mReadAdapter == null
                        || !mReadAdapter.hasSection(mBookSectionItems.get(indexOfSectionList + 1).getSectionIndex())) {
                    BookSectionItem bookSectionItem = mBookSectionItems.get(indexOfSectionList + 1);
                    loadSectionContent(bookSectionItem.getSectionIndex(), bookSectionItem.getSectionId(), false);
                }
            }
            if (indexOfSectionList - 1 >= 0) {
                if (mReadAdapter == null
                        || !mReadAdapter.hasSection(mBookSectionItems.get(indexOfSectionList - 1).getSectionIndex())) {
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
            doLoadData(bookSectionItem.getSectionIndex(), bookSectionItem.getSectionId());
        } else {
            isFirstChapterChange = false;
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
                        doLoadData(item.getSectionIndex(), item.getSectionId());
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
