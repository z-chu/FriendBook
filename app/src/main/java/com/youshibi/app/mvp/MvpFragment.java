package com.youshibi.app.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.youshibi.app.base.BaseFragment;
import com.youshibi.app.util.ToastUtil;


/**
 * Created by zchu on 16-11-17.
 */

public abstract class MvpFragment<P extends MvpPresenter> extends BaseFragment
        implements MvpView {
    protected P mPresenter;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter=null;
        }
    }

    @NonNull
    public abstract P createPresenter();

    @NonNull
    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void showToast(String message) {
        ToastUtil.showToast(message);
    }

    @Override
    public Context provideContext() {
        return this.getContext();
    }
}
