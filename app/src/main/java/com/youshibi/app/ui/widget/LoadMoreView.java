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
public class LoadMoreView extends FrameLayout {
    private int mStatus;

    private View mLoadingView;

    private View mErrorView;

    private View mTheEndView;

    private OnLoadMoreRetryListener mOnRetryListener;

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
                if (mStatus == Status.ERROR && mOnRetryListener != null) {
                    mOnRetryListener.onLoadMoreRetry(LoadMoreView.this);
                }
            }
        });
        setStatus(Status.GONE);
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        this.mStatus = status;
        change();
    }

    public boolean isTheEnd() {
        return mStatus == Status.THE_END;
    }

    public void makeMoreGone() {
        setStatus(Status.GONE);

    }

    public void makeMoreLoading() {
        setStatus(Status.LOADING);
    }

    public void makeMoreError() {
        setStatus(Status.ERROR);
    }

    public void makeTheEnd() {
        setStatus(Status.THE_END);
    }

    public boolean canLoadMore() {
        return mStatus == Status.GONE || mStatus == Status.ERROR;
    }

    private void change() {
        switch (mStatus) {
            case Status.GONE:

                mLoadingView.setVisibility(VISIBLE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(GONE);

                break;

            case Status.LOADING:

                mLoadingView.setVisibility(VISIBLE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(GONE);


                break;

            case Status.ERROR:

                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(VISIBLE);
                mTheEndView.setVisibility(GONE);


                break;

            case Status.THE_END:

                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(VISIBLE);


                break;
        }
    }


    public void setOnLoadMoreRetryListener(OnLoadMoreRetryListener listener) {
        this.mOnRetryListener = listener;
    }

    public class Status {
        public static final int GONE = 0;
        public static final int LOADING = 1;
        public static final int ERROR = 2;
        public static final int THE_END = 3;
    }

    public interface OnLoadMoreRetryListener {
        void onLoadMoreRetry(View view);
    }

}
