package com.youshibi.app.base;

import com.youshibi.app.ui.help.CommonAdapter;
import com.zchu.log.Logger;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Chu on 2017/5/28.
 */

public abstract class BaseListPresenter<V extends BaseListContract.View, M> extends BaseRxPresenter<V> implements BaseListContract.Presenter<V> {
    private int page;
    private CommonAdapter<M> adapter;

    @Override
    public void loadData(final boolean isRefresh) {
        page = 1;
        if (isViewAttached()) {
            getView().showLoading(isRefresh);
        }
        Subscription subscribe = doLoadData(isRefresh,page,getPageSize())
                .subscribe(new Subscriber<List<M>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        onLoadDataError(e, isRefresh);
                    }

                    @Override
                    public void onNext(List<M> data) {
                        onLoadDataSucceed(data, isRefresh);
                    }
                });
        addSubscription2Destroy(subscribe);

    }

    protected void onLoadDataError(Throwable e, boolean isRefresh) {
        Logger.e(e);
        if (isViewAttached()) {
            getView().showError(e.getMessage(), isRefresh);
        }
    }

    protected void onLoadDataSucceed(List<M> data, boolean isRefresh) {
        if (isViewAttached()) {
            if (!isRefresh || adapter == null) {
                adapter = createAdapter(data);
                getView().setAdapter(adapter);
                getView().showContent(isRefresh);
            } else {
                adapter.setNewData(data);
                getView().showContent(isRefresh);
            }
        }
    }


    protected abstract Observable<List<M>> doLoadData(boolean isRefresh,int page,int size);


    @Override
    public void loadMoreData() {
        if (page * getPageSize() >= getCount()&&isViewAttached()) {
                getView().showTheEnd();
        }else {
            page++;
            if (isViewAttached()) {
                getView().showMoreLoading();
            }
            Subscription subscribe = doLoadMoreData(page,getPageSize())
                    .subscribe(new Subscriber<List<M>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            onLoadMoreDataError(e);
                        }

                        @Override
                        public void onNext(List<M> data) {
                            onLoadMoreDataSucceed(data);
                        }
                    });
            addSubscription2Destroy(subscribe);
        }
    }

    /**
     * 当试图去加载下一页时，如果还有下一页，则调用
     */
    protected void onLoadMoreDataError(Throwable e) {
        Logger.e(e);
        if (isViewAttached()) {
            getView().showMoreError();
        }
    }

    protected void onLoadMoreDataSucceed(List<M> data) {
        if (isViewAttached()) {
            adapter.addData(data);
            getView().showMoreFrom();
        }
    }

    protected abstract Observable<List<M>> doLoadMoreData(int page,int size);

    protected abstract CommonAdapter<M> createAdapter(List<M> data);

    protected abstract int getPageSize() ;

    /**
     * 在 doLoadMoreData(int page,int size)时调用，返回0时只会加载1页
     */
    protected abstract long getCount();

    protected CommonAdapter<M> getAdapter() {
        return adapter;
    }

}
