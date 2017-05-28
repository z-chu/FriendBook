package com.youshibi.app.presentation.home;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.youshibi.app.ui.help.RecycleViewDivider;
import com.youshibi.app.ui.widget.LoadErrorView;
import com.youshibi.app.ui.widget.LoadMoreView;
import com.youshibi.app.util.DensityUtil;
import com.youshibi.app.util.DisplayUtil;
import com.youshibi.app.util.ToastUtil;

/**
 * Created by Chu on 2016/12/3.
 */

public class HomeFragment extends MvpFragment<HomePresenter> implements SwipeRefreshLayout.OnRefreshListener, LoadErrorView.OnRetryListener, HomeView {

    private static final String BUNDLE_BOOK_TYPE="book_type";

    private SwipeRefreshLayout contentView;
    private RecyclerView recyclerView;
    private LoadErrorView loadErrorView;
    private LoadMoreView loadMoreView;


    public static HomeFragment newInstance() {

        // Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        //  fragment.setArguments(args);
        return fragment;
    }

    public static HomeFragment newInstance(long bookType) {
        Bundle args = new Bundle();
        args.putLong(BUNDLE_BOOK_TYPE,bookType);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public HomePresenter createPresenter() {
        Bundle arguments = getArguments();
        long bookType=0;
        if(arguments!=null){
            bookType=arguments.getLong(BUNDLE_BOOK_TYPE);
        }
        return new HomePresenter(bookType);
    }

    @Override
    public int getLayoutId() {
        return R.layout._layout_list;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contentView = (SwipeRefreshLayout) view.findViewById(R.id.content_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        loadErrorView = (LoadErrorView) view.findViewById(R.id.load_error_view);
        contentView.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        loadErrorView.setOnRetryListener(this);
        contentView.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.addOnScrollListener(new OnLoadMoreScrollListener() {
            @Override
            public void onLoadMore(RecyclerView var1) {
                loadMoreData();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DisplayUtil.hasVirtualNavigationBar()) {
            recyclerView.setPadding(0, 0, 0, DisplayUtil.getNavigationBarHeight());
        }
        //GlideScrollPauseHelper.with(recyclerView);
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
            loadErrorView.makeLoading();
        }

    }

    @Override
    public void showError(String errorMsg, boolean isRefresh) {
        if (isRefresh) {
            ToastUtil.showToast(errorMsg);
        } else {
            contentView.post(new Runnable() {
                @Override
                public void run() {
                    contentView.setRefreshing(false);
                }
            });
            loadErrorView.makeError();
        }

    }

    @Override
    public void setAdapter(BaseQuickAdapter adapter) {
        loadMoreView = new LoadMoreView(getActivity());
        loadMoreView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(getContext(), 64)));
        adapter.addFooterView(loadMoreView);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showContent() {
        InContentAnim inContentAnim = new InContentAnim(contentView, loadErrorView);
        inContentAnim.start();
        contentView.post(new Runnable() {
            @Override
            public void run() {
                contentView.setRefreshing(false);
            }
        });
    }

    @Override
    public void showMoreLoading() {
        if (loadMoreView != null)
            loadMoreView.makeMoreLoading();
    }

    @Override
    public void showMoreError() {
        if (loadMoreView != null)
            loadMoreView.makeMoreError();
    }

    @Override
    public void showTheEnd() {
        if (loadMoreView != null)
            loadMoreView.makeTheEnd();
    }

    @Override
    public void showMoreFrom() {
        if (loadMoreView != null)
            loadMoreView.makeMoreLoading();
    }

    private void loadData(boolean isRefresh) {
        getPresenter().doLoadData(isRefresh);

    }

    private void loadMoreData() {
        getPresenter().doLoadMoreData();
    }
}
