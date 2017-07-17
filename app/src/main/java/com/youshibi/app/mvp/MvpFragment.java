package com.youshibi.app.mvp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.youshibi.app.base.BaseFragment;
import com.youshibi.app.util.ToastUtil;


/**
 * Created by zchu on 16-11-17.
 */

public abstract class MvpFragment<P extends MvpPresenter> extends BaseFragment implements MvpView {

    private P mPresenter;

    @Override
    public void showToast(String message) {
        ToastUtil.showToast(message);
    }

    /**
     * Fragment中获取Context的两种方法:getContext() 和getActivity()对比
     * https://stackoverflow.com/questions/32227146/what-is-different-between-getcontext-and-getactivity-from-fragment-in-support-li
     */
    @Override
    public Context provideContext() {
        return this.getContext();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.destroy();
            mPresenter=null;
        }
    }

    @NonNull
    public abstract P createPresenter();

    /**
     * 子类通过调用该方法，获得绑定的presenter
     * @return 绑定的presenter
     */
    protected P getPresenter() {
        if(mPresenter==null){
            mPresenter=createPresenter();
            mPresenter.attachView(this);
        }
        return mPresenter;
    }
}
