package com.youshibi.app.base;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrListenerAdapter;
import com.youshibi.app.AppManager;
import com.youshibi.app.R;
import com.youshibi.app.util.BitmapUtil;
import com.youshibi.app.util.InputMethodUtils;

/**
 * 作者: 赵成柱 on 2016/7/13.
 * 职责：为所有的activity封装功能
 */
public class BaseActivity extends BaseSuperActivity {
    /**
     * 窗体第一次获取焦点
     */
    private boolean isFirstFocus = true;

    private ImmersionBar mImmersionBar;
    private Drawable mDefaultWindowBackground;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置缓存
        View decorView = getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        //设置沉淀式状态栏
        mImmersionBar = ImmersionBar.with(this);
        initImmersionBar(ImmersionBar.with(this));
        if (isEnableSlideFinish()) {
            Slidr.attach(this, new SlidrConfig
                    .Builder()
                    .edge(true)
                    .edgeSize(0.18f)// The % of the screen that counts as the edge, default 18%
                    .listener(new SlidrListenerAdapter() {
                        @Override
                        public void onSlideStateChanged(int state) {
                            BaseActivity.this.onSlideStateChanged(state);
                        }

                        @Override
                        public void onSlideOpened() {
                            BaseActivity.this.onSlideCancel();
                        }

                        @Override
                        public void onSlideClosed() {
                            BaseActivity.this.onSlideClosed();
                        }
                    })
                    .build());
        }
    }

    protected void onSlideStateChanged(int state) {
        if (state == ViewDragHelper.STATE_DRAGGING) {
            Drawable windowBackground = getWindowBackground();
            if (windowBackground != null) {
                getWindow().setBackgroundDrawable(windowBackground);
            } else {
                getWindow().setBackgroundDrawable(getDefaultWindowBackground());
            }
        }
    }

    public void onSlideCancel(){
        getWindow().setBackgroundDrawable(getDefaultWindowBackground());
    }

    public void onSlideClosed(){
        InputMethodUtils.hideSoftInput(BaseActivity.this);
    }

    private Drawable windowBackground = null;

    public Drawable getWindowBackground() {
        if (windowBackground == null) {
            Activity beforeActivity = AppManager.getInstance().beforeActivity();
            if (beforeActivity != null) {
                Object tag = beforeActivity.getWindow().getDecorView().getTag();

                if (tag != null && tag instanceof Bitmap) {
                    windowBackground = BitmapUtil.bitmapToDrawable(getResources(), (Bitmap) tag);
                } else {
                    windowBackground = BitmapUtil.bitmapToDrawable(getResources(), getActivityBitmap(beforeActivity));
                }
            }
        }
        return windowBackground;
    }



    public Drawable getDefaultWindowBackground() {
        if(mDefaultWindowBackground==null) {
            int[] attrsArray = {android.R.attr.windowBackground};
            TypedArray typedArray = this.obtainStyledAttributes(attrsArray);
            mDefaultWindowBackground = typedArray.getDrawable(0);
            typedArray.recycle();
        }
        return mDefaultWindowBackground;
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
/*        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);*/
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    /**
     * 当窗体第一次获取到焦点会回调该方法
     */
    protected void onWindowFocusFirstObtain() {
    /*    if (isEnableSlideFinish()) {
            getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Activity beforeActivity = AppManager.getInstance().beforeActivity();
                    if (windowBackground == null) {
                        if (beforeActivity != null) {
                            windowBackground = BitmapUtil.bitmapToDrawable(getResources(), getActivityBitmap(beforeActivity));
                        }
                    }
                }
            }, 1000);
        }*/
    }

    protected void initImmersionBar(ImmersionBar immersionBar) {
        View statusBarView = findViewById(R.id.status_bar_view);
        if (statusBarView != null) {
            immersionBar
                    .statusBarView(statusBarView)
                    .init();
        } else {
            immersionBar
                    .fitsSystemWindows(true)
                    .statusBarColor(R.color.colorPrimaryDark)
                    .init();
        }


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImmersionBar.destroy();
    }
}
