package com.youshibi.app.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.youshibi.app.base.BaseActivity;
import com.youshibi.app.util.ToastUtil;


/**
 * Created by zchu on 16-11-17.
 */

public abstract class MvpActivity<P extends MvpPresenter> extends BaseActivity implements MvpView {
    protected P mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        return this;
    }
}
