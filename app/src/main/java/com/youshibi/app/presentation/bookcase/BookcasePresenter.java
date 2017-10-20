package com.youshibi.app.presentation.bookcase;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.umeng.analytics.MobclickAgent;
import com.youshibi.app.AppRouter;
import com.youshibi.app.base.BaseListPresenter;
import com.youshibi.app.data.DBManger;
import com.youshibi.app.data.DataManager;
import com.youshibi.app.data.bean.LatestChapter;
import com.youshibi.app.data.db.table.BookTb;
import com.youshibi.app.data.prefs.PreferencesHelper;
import com.youshibi.app.event.AddBook2BookcaseEvent;
import com.youshibi.app.event.BookcaseRefreshEvent;
import com.youshibi.app.rx.RxBus;
import com.youshibi.app.rx.SimpleSubscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chu on 2016/12/3.
 */

public class BookcasePresenter extends BaseListPresenter<BookcaseContract.View, BookTb> implements BookcaseContract.Presenter {

    private BaseQuickAdapter mAdapter;

    @BookcaseSort
    private int bookcaseSort;

    public BookcasePresenter() {
        bookcaseSort = PreferencesHelper.getInstance().getBookcaseSort();
    }

    @Override
    public void start() {
        super.start();
        if (isViewAttached()) {
            getView().addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    BookcaseAdapter bookcaseAdapter = (BookcaseAdapter) adapter;
                    if (bookcaseAdapter.isEditing()) {
                        bookcaseAdapter.selectedItem(position);
                    } else {
                        BookTb bookTb = (BookTb) adapter.getItem(position);
                        AppRouter.showReadActivity(view.getContext(), bookTb);
                    }
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
        addSubscription2Destroy(
                RxBus
                        .getDefault()
                        .toObservable(BookcaseRefreshEvent.class)
                        .subscribe(new SimpleSubscriber<BookcaseRefreshEvent>() {
                            @Override
                            public void onNext(BookcaseRefreshEvent event) {
                                loadData(true);
                            }
                        })
        );
    }

    private void loadLatestChapter() {
        List<BookTb> data = mAdapter.getData();
        final HashMap<String, BookTb> bookTbHashMap = new HashMap<>();
        if (data.size() > 0) {
            ArrayList<String> bookIds = new ArrayList<>();
            for (BookTb bookTb : data) {
                bookIds.add(bookTb.getId());
                bookTbHashMap.put(bookTb.getId(), bookTb);
            }
            Subscription subscribe = DataManager
                    .getInstance()
                    .getLatestChapter(bookIds)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SimpleSubscriber<List<LatestChapter>>() {
                        @Override
                        public void onNext(List<LatestChapter> latestChapter) {
                            boolean hasUpdate=false;
                            for (LatestChapter chapter : latestChapter) {
                                BookTb bookTb = bookTbHashMap.get(chapter.getBookId());
                                Integer sectionCount = bookTb.getSectionCount();
                                if(sectionCount!=null&&chapter.getChapterCount()>sectionCount){
                                    bookTb.setHasUpdate(true);
                                    DBManger.getInstance().updateBookTb(bookTb);
                                    hasUpdate=true;
                                }
                            }
                            if(hasUpdate){
                                loadData(true);
                            }
                        }
                    });
            addSubscription2Destroy(subscribe);

        }
    }

    @Override
    protected Observable<List<BookTb>> doLoadData(boolean isRefresh, int page, int size) {
        Observable<List<BookTb>> listObservable;

        switch (bookcaseSort) {
            case BookcaseSort.LATEST_READ_TIME:
            default:
                listObservable = DBManger.getInstance().loadBookTbOrderLatestRead();
                break;
            case BookcaseSort.MOST_READ_NUMBER:
                listObservable = DBManger.getInstance().loadBookTbOrderMostRead();
                break;
            case BookcaseSort.NAME:
                listObservable = DBManger.getInstance().loadBookTbOrderName();
                break;
            case BookcaseSort.CREATE_TIME:
                listObservable = DBManger.getInstance().loadBookTb();
                break;
        }
        return listObservable
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    protected void onLoadDataSucceed(List<BookTb> data, boolean isRefresh) {
        super.onLoadDataSucceed(data, isRefresh);
        if (!isRefresh) {
            loadLatestChapter();
        }
    }

    @Override
    protected void onLoadDataError(Throwable e, boolean isRefresh) {
        MobclickAgent.reportError(getView().provideContext(), e);
        super.onLoadDataError(e, isRefresh);
    }

    @Override
    protected Observable<List<BookTb>> doLoadMoreData(int page, int size) {
        return null;
    }


    @Override
    protected BaseQuickAdapter createAdapter(List<BookTb> data) {
        mAdapter = new BookcaseAdapter(data);
        mAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, final int position) {
                BookcaseAdapter bookcaseAdapter = (BookcaseAdapter) adapter;

                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getView().startDrag(position);
                    }
                }, 200);
                if (bookcaseAdapter.startEdit()) {
                    getView().showEditMode();
                    bookcaseAdapter.selectedItem(position);
                }
                return true;
            }
        });
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

    @Override
    public void deleteItems(List<BookTb> bookTbs) {
        if (bookTbs.size() == 1) {
            BookTb bookTb = bookTbs.get(0);
            DBManger.getInstance().deleteBookTb(bookTb);
            mAdapter.remove(mAdapter.getData().indexOf(bookTb));
        } else {
            DBManger.getInstance().deleteBookTbs(bookTbs);
            mAdapter.getData().removeAll(bookTbs);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void dispatchSortSpinnerItemSelected(int position) {
        switch (position) {
            case 0:
            default:
                setBookcaseSort(BookcaseSort.LATEST_READ_TIME);
                break;
            case 1:
                setBookcaseSort(BookcaseSort.MOST_READ_NUMBER);
                break;
            case 2:
                setBookcaseSort(BookcaseSort.NAME);
                break;
            case 3:
                setBookcaseSort(BookcaseSort.CREATE_TIME);
                break;
        }
    }

    @Override
    public int getDefaultSelectedSortSpinnerItem() {
        switch (bookcaseSort) {
            case BookcaseSort.LATEST_READ_TIME:
            default:
                return 0;
            case BookcaseSort.MOST_READ_NUMBER:
                return 1;
            case BookcaseSort.NAME:
                return 2;
            case BookcaseSort.CREATE_TIME:
                return 3;
        }

    }

    @Override
    public void finishEdit() {
        DBManger.getInstance().updateBookTbSort(mAdapter.getData());
    }


    private void setBookcaseSort(@BookcaseSort int bookcaseSort) {
        if (this.bookcaseSort != bookcaseSort) {
            this.bookcaseSort = bookcaseSort;
            PreferencesHelper.getInstance().setBookcaseSort(bookcaseSort);
            DBManger.getInstance().clearBookTbSort();
            loadData(true);
        }
    }


}
