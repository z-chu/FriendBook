package com.youshibi.app.base;

import com.youshibi.app.mvp.MvpView;

/**
 * 作者: 赵成柱 on 216/7/28 0028.
 *
 */

public interface BaseLceView<M> extends MvpView {

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
     * 设置数据
     * @param data
     */
    void setData(M data);

    /**
     * 显示内容
     */
    void showContent();

    /**
     * 加载数据  统一在这个方法调presenter.loadData()
     * @param isRefresh 是否为刷新
     */
    void loadData(boolean isRefresh);
}
