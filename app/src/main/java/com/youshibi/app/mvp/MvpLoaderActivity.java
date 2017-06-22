package com.youshibi.app.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.youshibi.app.base.BaseActivity;
import com.youshibi.app.util.ToastUtil;

/**
 * Created by Chu on 2016/11/17.
 *
 * 主要解决activity/Fragment重新创建时Present中的耗时线程的保留
 *
 * 通过loader延长presenter的生命周期，不会因为Activity/Fragment的配置改变导致重新创建而使presenter一同重新创建
 * 如：
 *
 * 当用户旋转屏幕、在后台时内存不足、改变语言设置、attache 一个外部显示器等，都会使Activity/Fragment重新创建
 * 这时有可能presenter中还有耗时线程还没有工作完，就需要延长presenter的生命周期来保留它们
 *
 * 注意：只有当onBindPresenter(presenter)被调用时才与Presenter绑定，此时Presenter才不为空
 */

public abstract class MvpLoaderActivity<P extends MvpPresenter> extends BaseActivity implements MvpView, LoaderManager.LoaderCallbacks<P> {

    protected P mPresenter;

    public abstract void onBindPresenter(P presenter);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Nullable
    public P getPresenter() {
        return mPresenter;
    }

    @NonNull
    public abstract P createPresenter();

    @Override
    public Loader<P> onCreateLoader(int i, Bundle bundle) {
        return new PresenterLoader<P>(this) {
            @Override
            P create() {
                return createPresenter();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<P> loader, P p) {
        if (mPresenter == null) {
            this.mPresenter = p;
            mPresenter.attachView(this);
            onBindPresenter(p);
        }
    }

    @Override
    public void onLoaderReset(Loader<P> loader) {
        mPresenter.destroy();
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
