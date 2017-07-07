package com.youshibi.app.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.youshibi.app.R;

/**
 * 作者: 赵成柱 on 2016/7/13.
 * 职责：为所有的activity封装功能
 */
public class BaseActivity extends BaseSuperActivity {
    /**
     * 窗体第一次获取焦点
     */
    private boolean isFirstFocus = true;

    protected SystemBarTintManager systemBarTintManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置沉淀式状态栏
        systemBarTintManager = new SystemBarTintManager(this);
        systemBarTintManager.setStatusBarTintEnabled(true);
        systemBarTintManager.setStatusBarTintColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }


    @Override
    public void onContentChanged() {
        super.onContentChanged();
        if (isEnableSlideFinish()) {
            Slidr.attach(this, new SlidrConfig
                    .Builder()
                    .edge(true)
                    .edgeSize(0.18f)// The % of the screen that counts as the edge, default 18%
                    .build());
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isFirstFocus) {
            onWindowFocusFirstObtain();
            isFirstFocus = false;
        }
    }

    /**
     * 当窗体第一次获取到焦点会回调该方法
     */
    protected void onWindowFocusFirstObtain() {
    }


    protected boolean isEnableSlideFinish() {
        return true;
    }

    public void bindOnClickLister(View rootView, View.OnClickListener listener, @IdRes int... ids) {
        for (int id : ids) {
            View view = rootView.findViewById(id);
            if (view != null) {
                view.setOnClickListener(listener);
            }
        }
    }

    public void bindOnClickLister(View.OnClickListener listener,@IdRes int... ids) {
        for (int id : ids) {
            View view = findViewById(id);
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
