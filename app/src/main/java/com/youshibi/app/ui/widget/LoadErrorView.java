package com.youshibi.app.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.youshibi.app.R;


/**
 * Created by z-chu on 2016/6/8.
 */
public class LoadErrorView extends FrameLayout implements View.OnClickListener {

    public static final int STATE_NONE = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_FINISH = 3;
    //单位dp
    private static final int ANIM_TRANSLATE_Y = 40;
    //动画持续时间
    private static final int ANIM_TIME_LONG = 500;

    private LayoutInflater mLayoutInflater;
    private View mLoadingView;
    private View mErrorView;
    private View mContentView;
    private int mContentLayoutId;
    private int mLoadingLayoutId;
    private int mErrorLayoutId;
    private int mState = STATE_NONE;

    private OnRetryListener mOnRetryListener;
    private OnViewCreatedListener mContentViewCreatedListener;
    private OnViewCreatedListener mLoadingViewCreatedListener;
    private OnViewCreatedListener mErrorViewCreatedListener = new OnViewCreatedListener() {
        @Override
        public void onViewCreated(@NonNull View view) {
            View viewById = view.findViewById(R.id.reload_view);
            if(viewById!=null) {
                viewById.setOnClickListener(LoadErrorView.this);
            }
        }
    };


    public LoadErrorView(Context context) {
        this(context, null);
    }

    public LoadErrorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView(context, context.obtainStyledAttributes(attrs, R.styleable.LoadErrorView, defStyleAttr, 0));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadErrorView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inflateView(context, context.obtainStyledAttributes(attrs, R.styleable.LoadErrorView, defStyleAttr, defStyleRes));

    }

    private void inflateView(Context context, TypedArray typedArray) {
        mLayoutInflater = LayoutInflater.from(context);
        mContentLayoutId = typedArray.getResourceId(R.styleable.LoadErrorView_contentLayout, 0);
        mLoadingLayoutId = typedArray.getResourceId(R.styleable.LoadErrorView_loadingLayout, 0);
        mErrorLayoutId = typedArray.getResourceId(R.styleable.LoadErrorView_errorLayout, 0);
        typedArray.recycle();
    }


    public void showLoading() {
        if (mState == STATE_LOADING) {
            return;
        }
        this.mState = STATE_LOADING;
        if (mLoadingView != null) {
            mLoadingView.setVisibility(VISIBLE);
        } else {
            if (mLoadingLayoutId != 0) {
                mLoadingView = mLayoutInflater.inflate(mLoadingLayoutId, this, false);
                addView(mLoadingView);
                if (mLoadingViewCreatedListener != null) {
                    mLoadingViewCreatedListener.onViewCreated(mLoadingView);
                }
            }
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(GONE);
        }

    }

    public void showError() {
        if (mState == STATE_ERROR) {
            return;
        }
        this.mState = STATE_ERROR;
        if (mLoadingView != null) {
            mLoadingView.setVisibility(GONE);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(VISIBLE);
        } else {
            if (mErrorLayoutId != 0) {
                mErrorView = mLayoutInflater.inflate(mErrorLayoutId, this, false);
                addView(mErrorView);
                if (mErrorViewCreatedListener != null) {
                    mErrorViewCreatedListener.onViewCreated(mErrorView);
                }
                // mErrorView.findViewById(R.id.reload_view).setOnClickListener(this);
            }
        }
    }

    public void showContent() {
        if (mState == STATE_FINISH) {
            return;
        }
        this.mState = STATE_FINISH;
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }

        if (mContentView != null || mContentLayoutId != 0) {
            if (mContentView == null) {
                mContentView = mLayoutInflater.inflate(mContentLayoutId, this, false);
                addView(mContentView, 0);
                if (mContentViewCreatedListener != null) {
                    mContentViewCreatedListener.onViewCreated(mContentView);
                }
            }
            mContentView.setVisibility(View.VISIBLE);
            startShowContentAnim(mContentView, mLoadingView);
        } else {
            if (mLoadingView != null) {
                mLoadingView.setVisibility(View.GONE);
            }
        }

    }

    private void startShowContentAnim(View contentView, final View loadingView) {
        AnimatorSet animatorSet = new AnimatorSet();
        if (contentView != null && contentView.getVisibility() == View.VISIBLE) {
            ObjectAnimator contentFadeIn = ObjectAnimator.ofFloat(contentView, View.ALPHA, 0f, 1f);
            ObjectAnimator contentTranslateIn = ObjectAnimator.ofFloat(contentView, View.TRANSLATION_Y,
                    ANIM_TRANSLATE_Y, 0);
            animatorSet.playTogether(contentFadeIn, contentTranslateIn);
        }

        if (loadingView != null && loadingView.getVisibility() == View.VISIBLE) {
            ObjectAnimator loadingFadeOut = ObjectAnimator.ofFloat(loadingView, View.ALPHA, 1f, 0f);
            ObjectAnimator loadingTranslateOut = ObjectAnimator.ofFloat(loadingView, View.TRANSLATION_Y, 0,
                    -ANIM_TRANSLATE_Y * 2);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loadingView.setVisibility(View.GONE);
                    loadingView.setAlpha(1f); // For future showLoading calls
                    loadingView.setTranslationY(0);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                }
            });
            animatorSet.playTogether(loadingFadeOut, loadingTranslateOut);

        }
        animatorSet.setDuration(ANIM_TIME_LONG);
        animatorSet.start();
    }

    public void onClick(View v) {
        if (mOnRetryListener != null) {
            //点击重新加载
            mOnRetryListener.onRetry(v);
        }
    }

    public void setLoadingLayoutId(@LayoutRes int layoutId) {
        checkIsLegalStatus();
        this.mLoadingLayoutId = layoutId;
    }

    public void setLoadingView(View view) {
        checkIsLegalStatus();
        if (view != null) {
            this.mLoadingView = view;
            addView(mLoadingView);
            if (mLoadingViewCreatedListener != null) {
                mLoadingViewCreatedListener.onViewCreated(mLoadingView);
            }

            mLoadingView.setVisibility(View.GONE);
        }
    }

    public void setErrorLayoutId(int layoutId) {
        checkIsLegalStatus();
        this.mErrorLayoutId = layoutId;
    }

    public void setErrorView(View view) {
        checkIsLegalStatus();
        if (view != null) {
            this.mErrorView = view;
            addView(mErrorView);
            if (mErrorViewCreatedListener != null) {
                mErrorViewCreatedListener.onViewCreated(mErrorView);
            }

            mErrorView.setVisibility(View.GONE);
        }
    }

    public void setContentLayoutId(int layoutId) {
        checkIsLegalStatus();
        this.mContentLayoutId = layoutId;
    }

    public void setContentView(View view) {
        checkIsLegalStatus();
        if (view != null) {
            this.mContentView = view;
            addView(mContentView, 0);
            if (mContentViewCreatedListener != null) {
                mContentViewCreatedListener.onViewCreated(mContentView);
            }
            mContentView.setVisibility(View.GONE);
        }
    }

    private void checkIsLegalStatus() {
        if (mState != STATE_NONE) {
            throw new IllegalStateException("Can not change view , because" + getClass().getSimpleName() + "\'s state is not STATE_NONE");
        }
    }


    public int getState() {
        return mState;
    }

    /**
     * 设置点击重试的回调，注意如果还设置ErrorViewCreatedListener，那setOnRetryListener设置的listener将会失效
     */
    public void setOnRetryListener(OnRetryListener listener) {
        this.mOnRetryListener = listener;
    }

    public void setLoadingViewCreatedListener(OnViewCreatedListener listener) {
        if (mLoadingView != null && mLoadingView.getParent() == this) {
            listener.onViewCreated(mContentView);
        } else {
            this.mLoadingViewCreatedListener = listener;
        }
    }

    public void setContentViewCreatedListener(OnViewCreatedListener listener) {
        if (mContentView != null && mContentView.getParent() == this) {
            listener.onViewCreated(mContentView);
        } else {
            this.mContentViewCreatedListener = listener;
        }
    }

    /**
     * 设置ErrorView 在创建时回调，注意设置完之后，setOnRetryListener设置的listener将会失效
     */
    public void setErrorViewCreatedListener(OnViewCreatedListener listener) {
        if (mErrorView != null && mErrorView.getParent() == this) {
            listener.onViewCreated(mErrorView);
        } else {
            this.mErrorViewCreatedListener = listener;
        }
    }

    public interface OnRetryListener {
        /**
         * 重新加载
         */
        void onRetry(View view);
    }

    public interface OnViewCreatedListener {
        void onViewCreated(@NonNull View view);
    }

}
