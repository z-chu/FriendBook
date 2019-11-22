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

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * author : zchu
 * date   : 2017/9/18
 * desc   :
 */

public class BookRankingPresenter extends BaseListPresenter<BaseListContract.View, Book> {

    private long channelId;
    private int count;

    public BookRankingPresenter(long channelId) {
        this.channelId = channelId;
    }

    @Override
    public void start() {
        super.start();
        if (isViewAttached()) {
            getView().addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    AppRouter.showBookDetailActivity(view.getContext(), ((Book) adapter.getItem(position)));
                }
            });
        }
    }

    @Override
    protected Observable<List<Book>> doLoadData(boolean isRefresh, int page, int size) {
        return DataManager
                .getInstance()
                .getChannelBookRanking(channelId, page, size)
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
        return DataManager
                .getInstance()
                .getChannelBookRanking(channelId, page, size)
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
    protected BaseQuickAdapter createAdapter(List<Book> data) {
        return new BookAdapter(data);
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
