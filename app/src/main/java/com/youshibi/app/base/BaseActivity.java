package com.youshibi.app.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.youshibi.app.R;

/**
 * 作者: 赵成柱 on 2016/7/13.
 * 职责：为所有的activity封装功能
 */
public class BaseActivity extends BaseSuperActivity{
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isFirstFocus) {
            onWindowFocusFirstObtain();
        }
    }

    /**
     * 当窗体第一次获取到焦点会回调该方法
     */
    protected void onWindowFocusFirstObtain() {}




}
