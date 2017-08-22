package com.youshibi.app.presentation.bookcase;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.youshibi.app.AppRouter;
import com.youshibi.app.R;
import com.youshibi.app.base.BaseListContract;
import com.youshibi.app.base.BaseListPresenter;
import com.youshibi.app.data.DBManger;
import com.youshibi.app.data.db.table.BookTb;
import com.youshibi.app.event.AddBook2BookcaseEvent;
import com.youshibi.app.rx.RxBus;
import com.youshibi.app.rx.SimpleSubscriber;
import com.youshibi.app.ui.help.CommonAdapter;
import com.youshibi.app.ui.help.CommonViewHolder;
import com.youshibi.app.util.GlideApp;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Chu on 2016/12/3.
 */

public class BookcasePresenter extends BaseListPresenter<BaseListContract.View, BookTb> {

    private CommonAdapter<BookTb> mAdapter;

    @Override
    public void start() {
        super.start();
        if (isViewAttached()) {
            getView().addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    BookTb bookTb = (BookTb) adapter.getItem(position);
                    AppRouter.showReadActivity(view.getContext(),bookTb.getId(),bookTb.getName(),0);
                   // AppRouter.showBookDetailActivity(view.getContext(), DataConvertUtil.bookTb2Book((BookTb) adapter.getItem(position)));
                }
            });
        }

        Subscription subscribe = RxBus
                .getDefault()
                .toObservable(AddBook2BookcaseEvent.class)
                .subscribe(new SimpleSubscriber<AddBook2BookcaseEvent>() {
                    @Override
                    public void onNext(AddBook2BookcaseEvent addBook2BookcaseEvent) {
                        mAdapter.addData(addBook2BookcaseEvent.bookTb);
                    }
                });
        addSubscription2Destroy(subscribe);
    }

    @Override
    protected Observable<List<BookTb>> doLoadData(boolean isRefresh, int page, int size) {
        return DBManger
                .getInstance()
                .loadBookTb()
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    protected Observable<List<BookTb>> doLoadMoreData(int page, int size) {
        return null;
    }


    @Override
    protected CommonAdapter<BookTb> createAdapter(List<BookTb> data) {
        mAdapter = new CommonAdapter<BookTb>(R.layout.grid_item_bookcase_book, data) {
            @Override
            protected void convert(CommonViewHolder helper, BookTb item) {
                GlideApp
                        .with(mContext)
                        .load(item.getCoverUrl())
                        .placeholder(R.drawable.ic_book_cover_default)
                        .into((ImageView) helper.getView(R.id.iv_cover));
            }
        };
        return mAdapter;
    }

    @Override
    protected int getPageSize() {
        return 0;
    }

    @Override
    protected long getCount() {
        return 0;
    }
}
