package com.youshibi.app.mvp.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.youshibi.app.base.BaseActivity;
import com.youshibi.app.mvp.MvpPresenter;
import com.youshibi.app.mvp.MvpView;
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
 * 注意：只有当onAttachPresenter()被调用时才与Presenter绑定，此时Presenter才不为空
 */

public abstract class MvpLoaderActivity<P extends MvpPresenter> extends BaseActivity implements MvpView, LoaderManager.LoaderCallbacks<P> {
    protected P mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //传入一个id，需要保证在一个Activity/Fragment内单一即可，不需要全局单一。这个id就是用来识别Loader的
        getSupportLoaderManager().initLoader(1, null, this);
    }

    public abstract void onAttachPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @NonNull
    public abstract P createPresenter();


    @NonNull
    public P getPresenter() {
        return mPresenter;
    }


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
            onAttachPresenter();
        }
    }

    @Override
    public void onLoaderReset(Loader<P> loader) {
        mPresenter.detachView();
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
