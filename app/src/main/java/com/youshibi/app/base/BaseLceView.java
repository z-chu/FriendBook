package com.youshibi.app.base;

import com.youshibi.app.mvp.MvpView;

/**
 * 作者: 赵成柱 on 216/7/28 0028.
 */

public interface BaseLceView extends MvpView {


    /**
     * 显示内容
     */
    void showContent();

    /**
     * 显示加载中
     */
    void showLoading();

    /**
     * 显示加载出错
     *
     * @param errorMsg  异常信息
     */
    void showError(String errorMsg);


}
