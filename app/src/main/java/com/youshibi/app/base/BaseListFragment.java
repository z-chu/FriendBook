package com.youshibi.app.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youshibi.app.R;
import com.youshibi.app.mvp.MvpFragment;
import com.youshibi.app.ui.anim.InContentAnim;
import com.youshibi.app.ui.help.OnLoadMoreScrollListener;
import com.youshibi.app.ui.widget.LoadErrorView;
import com.youshibi.app.ui.widget.LoadMoreView;
import com.youshibi.app.util.ToastUtil;


/**
 * Created by Chu on 2016/12/3.
 */

public abstract class BaseListFragment<P extends BaseListContract.Presenter> extends MvpFragment<P> implements SwipeRefreshLayout.OnRefreshListener, LoadErrorView.OnRetryListener, BaseListContract.View {

    protected SwipeRefreshLayout contentView;
    protected RecyclerView recyclerView;
    protected LoadErrorView loadErrorView;
    protected LoadMoreView loadMoreView;
    protected InContentAnim inContentAnim;

    @Override
    public int getLayoutId() {
        return R.layout._layout_list;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contentView = view.findViewById(R.id.content_view);
        recyclerView = view.findViewById(R.id.recycler_view);
        loadErrorView =  view.findViewById(R.id.load_error_view);
        contentView.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        loadErrorView.setOnRetryListener(this);
        contentView.setOnRefreshListener(this);
        recyclerView.addOnScrollListener(new OnLoadMoreScrollListener() {
            @Override
            public void onLoadMore(RecyclerView var1) {
                loadMoreData();
            }
        });
        setRecyclerView(recyclerView);
        getPresenter().start();


    }

    public void setRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
    }

    @Override
    public void onShow(boolean isFirstShow) {
        super.onShow(isFirstShow);
        if (isFirstShow) {
            loadData(false);
        }
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    public void onRetry(View view) {
        loadData(false);
    }

    @Override
    public void showLoading(boolean isRefresh) {
        if (isRefresh) {
            contentView.post(new Runnable() {
                @Override
                public void run() {
                    contentView.setRefreshing(true);
                }
            });
        } else {
            contentView.setVisibility(View.GONE);
            loadErrorView.showLoading();
        }

    }


    @Override
    public void showError(String errorMsg, boolean isRefresh) {
        if (isRefresh) {
            ToastUtil.showToast(errorMsg);
            contentView.post(new Runnable() {
                @Override
                public void run() {
                    contentView.setRefreshing(false);
                }
            });
        } else {
            loadErrorView.showError();
        }

    }

    @Override
    public void setAdapter(BaseQuickAdapter adapter) {
        loadMoreView = new LoadMoreView(getActivity());
        loadMoreView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.load_more_height)));
        loadMoreView.setOnLoadMoreRetryListener(new LoadMoreView.OnLoadMoreRetryListener() {
            @Override
            public void onLoadMoreRetry(View view) {
                loadMoreData();
            }
        });
        adapter.addFooterView(loadMoreView);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showContent(boolean isRefresh) {
        if (!isRefresh) {
            if (inContentAnim == null) {
                inContentAnim = new InContentAnim(contentView, loadErrorView);
            }
            inContentAnim.start();
        }
        contentView.post(new Runnable() {
            @Override
            public void run() {
                contentView.setRefreshing(false);
            }
        });
    }

    @Override
    public void showMoreLoading() {
        if (loadMoreView != null) {
            loadMoreView.showLoading();
        }
    }

    @Override
    public void showMoreError() {
        if (loadMoreView != null) {
            loadMoreView.showError();
        }

    }

    @Override
    public void showTheEnd() {
        if (loadMoreView != null) {
            loadMoreView.showTheEnd();
        }
    }

    @Override
    public void showMoreFrom() {
        if (loadMoreView != null) {
            loadMoreView.showNone();
        }
    }


    protected void loadData(boolean isRefresh) {
        if (loadMoreView == null || loadMoreView.canLoadMore() || loadMoreView.isTheEnd()) {
            getPresenter().loadData(isRefresh);
        } else {
            contentView.post(new Runnable() {
                @Override
                public void run() {
                    contentView.setRefreshing(false);
                }
            });
        }

    }

    protected void loadMoreData() {
        if (loadMoreView != null && loadMoreView.canLoadMore()) {
            getPresenter().loadMoreData();
        }
    }

    @Override
    public void addOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        recyclerView.addOnItemTouchListener(listener);
    }


}
