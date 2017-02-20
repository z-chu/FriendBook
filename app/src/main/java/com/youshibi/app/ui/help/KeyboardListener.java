package com.youshibi.app.ui.help;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * 作者: 赵成柱 on 2016/8/24 0024.
 * 用于监听软件盘的弹出和收起
 * 主要根据软件盘的变化对布局的影响，来判断弹出和收起。
 */
public class KeyboardListener implements  View.OnLayoutChangeListener {
    private OnKeyboardListener mOnKeyboardListener;
    private int keyboardHeight;
    private KeyboardListener(@NonNull final View rootLayout, @NonNull OnKeyboardListener onKeyboardListener) {
        if (rootLayout == null) {
            throw new NullPointerException("rootLayout Can't be null");
        } else {
            keyboardHeight= rootLayout.getContext().getResources().getDisplayMetrics().heightPixels/3;
        }
        if (onKeyboardListener == null) {
            throw new NullPointerException("onKeyboardListener Can't be null");
        } else {
            mOnKeyboardListener = onKeyboardListener;
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyboardHeight)) {
            mOnKeyboardListener.onShow();
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyboardHeight)) {
            mOnKeyboardListener.onHide();
        }
    }


    public interface OnKeyboardListener {
        void onShow();

        void onHide();
    }


    /**
     *
     * @param bottomChangeView 一个在软件盘弹出是bottom会发生改变的View，如：被软件键盘向上顶的View,布局的根View。如果没有监听到，说明该View的bottom值没有随软键盘的弹出而改变
     * @param onKeyboardListener
     */
    public static void setListener(View bottomChangeView, OnKeyboardListener onKeyboardListener) {
        bottomChangeView.addOnLayoutChangeListener(new KeyboardListener(bottomChangeView, onKeyboardListener));

    }

}
