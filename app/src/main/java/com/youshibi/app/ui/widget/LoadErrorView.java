package com.youshibi.app.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.youshibi.app.R;


/**
 * Created by z-chu on 2016/6/8.
 *
 */
public class LoadErrorView extends FrameLayout implements  View.OnClickListener {

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

        LayoutInflater.from(context).inflate(R.layout.merge_loading_error, this, true);
        mLoadingView = findViewById(R.id.loading_view);
        mErrorView = findViewById(R.id.error_view);
        mErrorView.findViewById(R.id.reload_view).setOnClickListener(this);
        makeGone();

    }


    public void makeGone() {
        mLoadingView.setVisibility(GONE);
        mErrorView.setVisibility(GONE);
    }

    public void makeLoading() {
        mLoadingView.setVisibility(VISIBLE);
        mErrorView.setVisibility(GONE);
    }

    public void makeError() {
        mLoadingView.setVisibility(GONE);
        mErrorView.setVisibility(VISIBLE);
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
        /**重新加载*/
        void onRetry(View view);
    }


}
