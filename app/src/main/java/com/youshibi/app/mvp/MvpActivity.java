package com.youshibi.app.mvp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.youshibi.app.base.BaseActivity;
import com.youshibi.app.util.ToastUtil;


/**
 * Created by zchu on 16-11-17.
 */

public abstract class MvpActivity<P extends MvpPresenter> extends BaseActivity implements MvpView {

    private P mPresenter;

    @Override
    public void showToast(String message) {
        ToastUtil.showToast(message);
    }

    @Override
    public Context provideContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
