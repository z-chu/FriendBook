package com.youshibi.app.base;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youshibi.app.mvp.MvpPresenter;
import com.youshibi.app.mvp.MvpView;

/**
 * Created by Chu on 2016/12/4.
 */

public interface BaseListContract {
     interface  View extends MvpView {
        /**
         * 显示加载中
         * @param isRefresh 是否为刷新
         */
        void showLoading(boolean isRefresh);

        /**
         * 显示加载出错
         * @param errorMsg 异常信息
         * @param isRefresh 是否为刷新
         */
        void showError(String errorMsg, boolean isRefresh);

        /**
         * 设置adapter
         */
        void setAdapter(BaseQuickAdapter adapter);

        /**
         * 显示内容
         */
        void showContent(boolean isRefresh);


        /**
         * 显示加载更多
         */
        void showMoreLoading();

        /**
         * 显示加载更多出错
         */
        void showMoreError();

        /**
         * 显示没有更多内容
         */
        void showTheEnd();


        /**
         * 设置回最初状态，显示加载的数据
         */
        void showMoreFrom();

         void addOnItemTouchListener(RecyclerView.OnItemTouchListener listener);

     }

    interface Presenter<V extends BaseListContract.View> extends MvpPresenter<V> {

        void loadData(boolean isRefresh);

        void loadMoreData();
    }
}
