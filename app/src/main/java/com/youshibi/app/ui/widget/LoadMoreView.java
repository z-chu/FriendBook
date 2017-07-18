package com.youshibi.app.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.youshibi.app.R;

/**
 * Created by z-chu on 2016/6/8.
 */
public class LoadMoreView extends FrameLayout {

    public static final int STATE_GONE = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_THE_END = 3;

    private View mLoadingView;
    private View mErrorView;
    private View mTheEndView;
    private OnLoadMoreRetryListener mOnRetryListener;
    private int mState = STATE_GONE;

    public LoadMoreView(Context context) {
        this(context, null);
    }

    public LoadMoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View rootView = LayoutInflater.from(context).inflate(R.layout.merge_load_more, this, true);

        mLoadingView = findViewById(R.id.loading_view);
        mErrorView = findViewById(R.id.error_view);
        mTheEndView = findViewById(R.id.end_view);
        rootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mState == STATE_ERROR && mOnRetryListener != null) {
                    mOnRetryListener.onLoadMoreRetry(LoadMoreView.this);
                }
            }
        });
        change();
    }

    public int getState() {
        return mState;
    }

    private void setState(int status) {
        if (this.mState != status) {
            this.mState = status;
            change();
        }
    }

    public boolean isTheEnd() {
        return mState == STATE_THE_END;
    }

    public void showNone() {
        setState(STATE_GONE);

    }

    public void showLoading() {
        setState(STATE_LOADING);
    }

    public void showError() {
        setState(STATE_ERROR);
    }

    public void showTheEnd() {
        setState(STATE_THE_END);
    }

    public boolean canLoadMore() {
        return mState == STATE_GONE || mState == STATE_ERROR;
    }

    private void change() {
        switch (mState) {
            case STATE_GONE:
                mLoadingView.setVisibility(VISIBLE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(GONE);
                break;

            case STATE_LOADING:
                mLoadingView.setVisibility(VISIBLE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(GONE);
                break;

            case STATE_ERROR:
                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(VISIBLE);
                mTheEndView.setVisibility(GONE);
                break;

            case STATE_THE_END:
                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(VISIBLE);
                break;
        }
    }


    public void setOnLoadMoreRetryListener(OnLoadMoreRetryListener listener) {
        this.mOnRetryListener = listener;
    }


    public interface OnLoadMoreRetryListener {
        void onLoadMoreRetry(View view);
    }

}
