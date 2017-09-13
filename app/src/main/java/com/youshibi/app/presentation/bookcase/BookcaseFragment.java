package com.youshibi.app.presentation.bookcase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.umeng.analytics.MobclickAgent;
import com.youshibi.app.AppRouter;
import com.youshibi.app.R;
import com.youshibi.app.base.BaseListFragment;
import com.youshibi.app.util.DensityUtil;

/**
 * Created by Chu on 2016/12/3.
 */

public class BookcaseFragment extends BaseListFragment<BookcasePresenter> {


    public static BookcaseFragment newInstance() {
        return new BookcaseFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bookcase;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.inflateMenu(R.menu.bookcase);
        toolbar.getMenu().findItem(R.id.action_search).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AppRouter.showSearchActivity(getContext());
                return true;
            }
        });
    }

    @Override
    public void setRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        int i = getResources().getDisplayMetrics().widthPixels;
        int y = DensityUtil.dp2px(getContext(), 90);
        int p = DensityUtil.dp2px(getContext(), 15);
        int padding = (3 * y + 2 * 3 * p + 2 * p - i) / (2 * 3);

        recyclerView.setPadding(
                padding,
                DensityUtil.dp2px(getContext(), 8),
                padding,
                DensityUtil.dp2px(getContext(), 8)
        );
    }


    @Override
    public void setAdapter(BaseQuickAdapter adapter) {
        adapter.setEmptyView(R.layout.view_empty_bookcase, recyclerView);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            MobclickAgent.onPageEnd(getClass().getPackage().getName()+".BookcaseFragment");
        }else{
            MobclickAgent.onPageStart(getClass().getPackage().getName()+".BookcaseFragment");
        }
    }

    @NonNull
    @Override
    public BookcasePresenter createPresenter() {
        return new BookcasePresenter();
    }

}
