package com.youshibi.app.base;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
    private BaseQuickAdapter adapter;

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
                if(data.size()<getPageSize()){
                    getView().showTheEnd();
                }
            } else {
                adapter.setNewData(data);
                getView().showContent(isRefresh);
                if(data.size()<getPageSize()){
                    getView().showTheEnd();
                }
            }
        }
    }


    protected abstract Observable<List<M>> doLoadData(boolean isRefresh,int page,int size);


    @Override
    public void loadMoreData() {
        if (page * getPageSize() >= getCount()&&isViewAttached()) {
                getView().showTheEnd();
        }else {
            if (isViewAttached()) {
                getView().showMoreLoading();
            }
            Subscription subscribe = doLoadMoreData(page+1,getPageSize())
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
                            page++;
                        }
                    });
            addSubscription2Destroy(subscribe);
        }
    }


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

    protected abstract BaseQuickAdapter createAdapter(List<M> data);

    protected abstract int getPageSize() ;

    /**
     * 在 doLoadMoreData(int page,int size)时调用，返回0时只会加载1页
     */
    protected abstract long getCount();

    protected BaseQuickAdapter getAdapter() {
        return adapter;
    }

}
