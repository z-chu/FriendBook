package com.youshibi.app.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.youshibi.app.R;


/**
 * Created by z-chu on 2016/6/8.
 */
public class LoadErrorView extends FrameLayout implements View.OnClickListener {

    public static final int STATE_GONE = 1;
    public static final int STATE_LOADING = 2;
    public static final int STATE_ERROR = 3;
    public static final int STATE_FINISH = 4;

    private int mState;

    private ViewStub stubViewLoading;
    private ViewStub stubViewError;
    private View mLoadingView;
    private View mErrorView;
    private View mContentView;

    //单位dp
    private static final int ANIM_TRANSLATE_Y = 40;
    //动画持续时间
    private static final int ANIM_TIME_LONG = 500;

    private int mLayoutResource;

    private OnRetryListener mOnRetryListener;


    public LoadErrorView(Context context) {
        this(context, null);
    }

    public LoadErrorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.LoadErrorView, defStyleAttr, 0);
        inflateView(context, typedArray);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadErrorView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.LoadErrorView, defStyleAttr, defStyleRes);
        inflateView(context, typedArray);

    }

    private void inflateView(Context context, TypedArray typedArray) {
        mLayoutResource = typedArray.getResourceId(R.styleable.LoadErrorView_layout, 0);
        typedArray.recycle();
        View view = LayoutInflater.from(context).inflate(R.layout.merge_loading_error, this, true);
        stubViewLoading = (ViewStub) view.findViewById(R.id.stub_view_loading);
        stubViewError = (ViewStub) view.findViewById(R.id.stub_view_error);
        //  makeLoading();
    }


    public void makeGone() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(GONE);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(GONE);
        }
    }

    public void makeLoading() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(VISIBLE);
        } else {
            stubViewLoading.inflate();
            mLoadingView = findViewById(R.id.loading_view);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(GONE);
        }
    }

    public void makeError() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(GONE);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(VISIBLE);
        } else {
            stubViewError.inflate();
            mErrorView = findViewById(R.id.error_view);
            mErrorView.findViewById(R.id.reload_view).setOnClickListener(this);
        }
    }

    public void showContent() {
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }
        if (mLayoutResource != 0) {
            if (mContentView == null) {
                mContentView = LayoutInflater.from(getContext()).inflate(mLayoutResource, this, false);
                addView(mContentView,0);
        }
            startShowContentAnim();
        } else {
            if (mLoadingView != null) {
                mLoadingView.setVisibility(View.GONE);
            }
        }


    }

    private void startShowContentAnim() {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator contentFadeIn = ObjectAnimator.ofFloat(mContentView, View.ALPHA, 0f, 1f);
        ObjectAnimator contentTranslateIn = ObjectAnimator.ofFloat(mContentView, View.TRANSLATION_Y,
                ANIM_TRANSLATE_Y, 0);
        animatorSet.playTogether(contentFadeIn, contentTranslateIn);
        if (mLoadingView.getVisibility() == View.VISIBLE) {
            ObjectAnimator loadingFadeOut = ObjectAnimator.ofFloat(mLoadingView, View.ALPHA, 1f, 0f);
            ObjectAnimator loadingTranslateOut = ObjectAnimator.ofFloat(mLoadingView, View.TRANSLATION_Y, 0,
                    -ANIM_TRANSLATE_Y);
            animatorSet.playTogether(loadingFadeOut, loadingTranslateOut);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoadingView.setVisibility(View.GONE);
                    mLoadingView.setAlpha(1f); // For future showLoading calls
                    mLoadingView.setTranslationY(0);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                }
            });
        }
        animatorSet.setDuration(ANIM_TIME_LONG);
        animatorSet.start();
    }

    public void setOnRetryListener(OnRetryListener listener) {
        this.mOnRetryListener = listener;
    }

    public void onClick(View v) {
        if (mOnRetryListener != null) {
            //点击重新加载
            mOnRetryListener.onRetry(v);
        }
    }

    public interface OnRetryListener {
        /**
         * 重新加载
         */
        void onRetry(View view);
    }


}
