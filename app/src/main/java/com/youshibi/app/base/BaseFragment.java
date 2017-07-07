package com.youshibi.app.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者: 赵成柱 on 2016/7/26 0026.
 * 职责：为所有的Fragment封装功能
 */
public abstract class BaseFragment extends BaseSuperFragment {


    /**
     * 第一次暴露在外
     */
    protected boolean isFirstShow = true;

    /**
     * onViewCreated是否调用结束
     */
    protected boolean isViewCreated = false;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isFirstShow",isFirstShow);
        outState.putBoolean("isViewCreated",isViewCreated);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) {
            isFirstShow = savedInstanceState.getBoolean("isFirstShow",isFirstShow);
            isViewCreated = savedInstanceState.getBoolean("isViewCreated",isViewCreated);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewCreated = true;
        if (getUserVisibleHint()) {
            onShow(true);
            isFirstShow = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated=false;
        isFirstShow=true;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public abstract int getLayoutId();


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(isViewCreated) {
                onShow(isFirstShow);
                isFirstShow = false;
            }
        } else {
            onHide();
        }
    }

    /**
     * 当fragment暴露在外
     *
     * @param isFirstShow 是否为第一次显示
     */
    public void onShow(boolean isFirstShow) {

    }


    /**
     * 当fragment隐藏
     */
    public void onHide() {
    }


    public void bindOnClickLister(View rootView, View.OnClickListener listener, @IdRes int... ids) {
        for (int id : ids) {
            View view = rootView.findViewById(id);
            if (view != null) {
                view.setOnClickListener(listener);
            }
        }
    }

    public void bindOnClickLister(View.OnClickListener listener,View... views) {
        for (View view : views) {
            view.setOnClickListener(listener);
        }
    }

}
