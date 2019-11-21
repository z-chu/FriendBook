package com.youshibi.app.presentation.book;

import android.view.View;

import com.youshibi.app.AppRouter;
import com.youshibi.app.R;
import com.youshibi.app.base.BaseRxPresenter;
import com.youshibi.app.data.DataManager;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.data.bean.BookChapter;
import com.youshibi.app.rx.SimpleSubscriber;
import com.youshibi.app.ui.help.CommonAdapter;
import com.youshibi.app.ui.help.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author : zchu
 * date   : 2017/9/19
 * desc   :
 */

public class BookCatalogPresenter extends BaseRxPresenter<BookCatalogContract.View> implements BookCatalogContract.Presenter {

    private static final int PAGE_SIZE = 50;

    private Book mBook;
    private int mSectionCount;

    private CommonAdapter<BookChapter> bookSectionAdapter;
    private List<String> sectionData;

    public BookCatalogPresenter(Book book, int sectionCount) {
        this.mBook = book;
        this.mSectionCount = sectionCount;
    }

    @Override
    public void start() {
        super.start();
        if (bookSectionAdapter == null) {
            loadData(1);
        } else {
            getView().setListAdapter(bookSectionAdapter);
        }
        if (sectionData == null) {
            sectionData = createSectionData(mSectionCount);
        }
        getView().setSectionData(sectionData);
    }

    @Override
    public void loadData(int page) {
        DataManager
                .getInstance()
                .getBookSectionList(mBook.getId(), true, page, PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<List<BookChapter>>() {
                    @Override
                    public void onNext(List<BookChapter> bookSectionItems) {
                        if (isViewAttached()) {
                            setData(bookSectionItems);

                        }

                    }
                });

    }

    private void setData(List<BookChapter> bookSectionItems) {
        if (bookSectionAdapter == null) {
            bookSectionAdapter = new CommonAdapter<BookChapter>(R.layout.list_item_book_section, bookSectionItems) {
                @Override
                protected void convert(CommonViewHolder helper, final BookChapter item) {
                    helper.setText(R.id.tv_section_name, item.getChapterName());
                    helper.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                             AppRouter.showReadActivity(v.getContext(), mBook, item.getChapterIndex(), item.getChapterId());
                        }
                    });
                }
            };
            getView().setListAdapter(bookSectionAdapter);
        }
        bookSectionAdapter.setNewData(bookSectionItems);
    }

    private List<String> createSectionData(int sectionCount) {
        ArrayList<String> sectionTitles = new ArrayList<>();

        int size = sectionCount / PAGE_SIZE;
        for (int i = 0; i < size; i++) {
            sectionTitles.add(i * PAGE_SIZE + 1 + "-" + ((1 + i) * PAGE_SIZE) + "章");
        }
        if (PAGE_SIZE * size < sectionCount) {
            sectionTitles.add(1 + size * PAGE_SIZE + "-" + sectionCount + "章");
        }
        return sectionTitles;
    }

}
