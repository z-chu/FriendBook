package com.youshibi.app.base;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.youshibi.app.AppManager;
import com.youshibi.app.R;
import com.youshibi.app.util.BitmapUtil;

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
                    .listener(new SlidrListener() {
                        @Override
                        public void onSlideStateChanged(int state) {
                            if (state == 1) {
                                getWindow().setBackgroundDrawable(getWindowBackground());
                            }

                        }

                        @Override
                        public void onSlideChange(float percent) {

                        }

                        @Override
                        public void onSlideOpened() {

                        }

                        @Override
                        public void onSlideClosed() {

                        }
                    })
                    .build());
        }
    }

    private Drawable windowBackground = null;

    public Drawable getWindowBackground() {
        Activity beforeActivity = AppManager.getInstance().beforeActivity();
        if (beforeActivity != null) {
            windowBackground = BitmapUtil.bitmapToDrawable(getResources(), getActivityBitmap(beforeActivity));
        }
        return windowBackground;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isFirstFocus) {
            onWindowFocusFirstObtain();
            isFirstFocus = false;
        }
    }

    public static Bitmap getActivityBitmap(Activity activity) {
        View view = activity.getWindow().getDecorView();
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
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

    public void bindOnClickLister(View.OnClickListener listener, @IdRes int... ids) {
        for (int id : ids) {
            View view = findViewById(id);
            if (view != null) {
                view.setOnClickListener(listener);
            }
        }
    }

    public void bindOnClickLister(View.OnClickListener listener, View... views) {
        for (View view : views) {
            view.setOnClickListener(listener);
        }
    }

}
