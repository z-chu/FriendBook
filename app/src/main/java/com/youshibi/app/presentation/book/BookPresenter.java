package com.youshibi.app.presentation.book;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.youshibi.app.AppRouter;
import com.youshibi.app.base.BaseListContract;
import com.youshibi.app.base.BaseListPresenter;
import com.youshibi.app.data.DataManager;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.data.bean.DataList;
import com.youshibi.app.ui.help.CommonAdapter;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Chu on 2016/12/3.
 */

public class BookPresenter extends BaseListPresenter<BaseListContract.View, Book> {
    private long bookType;

    public BookPresenter(long bookType) {
        this.bookType = bookType;
    }

    @Override
    public void start() {
        super.start();
        getView().addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                AppRouter.showBookDetailActivity(view.getContext(), ((Book) adapter.getItem(position)));
            }
        });
    }

    @Override
    protected Observable<List<Book>> doLoadData(boolean isRefresh) {
        return DataManager
                .getInstance()
                .getBookList(getPage(), getPageSize(), bookType)
                .map(new Func1<DataList<Book>, List<Book>>() {
                    @Override
                    public List<Book> call(DataList<Book> bookDataList) {
                        setCount(bookDataList.Count);
                        return bookDataList.DataList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    protected Observable<List<Book>> doLoadMoreData() {
        return DataManager.getInstance()
                .getBookList(getPage(), getPageSize(), bookType)
                .map(new Func1<DataList<Book>, List<Book>>() {
                    @Override
                    public List<Book> call(DataList<Book> bookDataList) {
                        return bookDataList.DataList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    protected CommonAdapter<Book> createAdapter(List<Book> bookItems) {
        return new BookAdapter(bookItems);
    }


}
