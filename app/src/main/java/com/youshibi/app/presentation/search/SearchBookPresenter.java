package com.youshibi.app.presentation.search;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.youshibi.app.AppRouter;
import com.youshibi.app.base.BaseListContract;
import com.youshibi.app.base.BaseListPresenter;
import com.youshibi.app.data.DataManager;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.data.bean.DataList;
import com.youshibi.app.presentation.book.BookAdapter;
import com.youshibi.app.ui.help.CommonAdapter;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Chu on 2016/12/3.
 */

class SearchBookPresenter extends BaseListPresenter<BaseListContract.View, Book> {
    private String keyword;
    private int count;

    public SearchBookPresenter(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void start() {
        super.start();
        if (isViewAttached()) {
            getView().addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                   /* Book book = (Book) adapter.getItem(position);
                    AppRouter.showReadActivity(view.getContext(), book.getId(), book.getName(), 0);*/
                    AppRouter.showBookDetailActivity(view.getContext(), ((Book) adapter.getItem(position)));
                }
            });
        }
    }

    @Override
    protected Observable<List<Book>> doLoadData(boolean isRefresh, int page, int size) {
        return DataManager
                .getInstance()
                .searchBook(keyword, page, size)
                .map(new Func1<DataList<Book>, List<Book>>() {
                    @Override
                    public List<Book> call(DataList<Book> bookDataList) {
                        count = bookDataList.count;
                        return bookDataList.data_list;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    protected Observable<List<Book>> doLoadMoreData(int page, int size) {
        return DataManager.getInstance()
                .searchBook(keyword, page, size)
                .map(new Func1<DataList<Book>, List<Book>>() {
                    @Override
                    public List<Book> call(DataList<Book> bookDataList) {
                        return bookDataList.data_list;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    protected CommonAdapter<Book> createAdapter(List<Book> bookItems) {
        return new BookAdapter(bookItems);
    }

    @Override
    protected int getPageSize() {
        return 15;
    }

    @Override
    protected long getCount() {
        return count;
    }


}
