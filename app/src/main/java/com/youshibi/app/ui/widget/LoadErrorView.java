package com.youshibi.app.ui.widget;

import android.content.Context;
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

    private ViewStub stubViewLoading;
    private ViewStub stubViewError;
    private View mLoadingView;
    private View mErrorView;

    private OnRetryListener mOnRetryListener;


    public LoadErrorView(Context context) {
        this(context, null);
    }

    public LoadErrorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView(context);
    }

    private void inflateView(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.merge_loading_error, this, true);
        stubViewLoading = (ViewStub) view.findViewById(R.id.stub_view_loading);
        stubViewError = (ViewStub) view.findViewById(R.id.stub_view_error);
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
