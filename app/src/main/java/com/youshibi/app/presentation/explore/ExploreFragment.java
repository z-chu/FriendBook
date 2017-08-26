package com.youshibi.app.presentation.explore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.youshibi.app.AppRouter;
import com.youshibi.app.R;
import com.youshibi.app.data.bean.BookType;
import com.youshibi.app.mvp.MvpLoaderFragment;
import com.youshibi.app.ui.help.BaseFragmentAdapter;
import com.youshibi.app.util.CountEventHelper;

import java.util.ArrayList;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Chu on 2017/5/28.
 */

public class ExploreFragment extends MvpLoaderFragment<ExploreContract.Presenter> implements ExploreContract.View, View.OnClickListener {

    private ViewPager viewPager;
    private AppBarLayout appbar;
    //   private Toolbar toolbar;
    private TabLayout tab;
    private View ivBookTypeMore;
    private MaterialProgressBar progress;
    private AppCompatImageView ivBookTypeRetry;


    private boolean isShow = false;
    private boolean isBindPresenter = false;


    private ArrayList<BookType> alwaySelectedBookTypes = new ArrayList<>();
    private ArrayList<BookType> selectedBookTypes = new ArrayList<>();
    private ArrayList<BookType> unselectedBookTypes = new ArrayList<>();


    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_explore;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = (ViewPager) view.findViewById(R.id.vp_explore);
        appbar = (AppBarLayout) view.findViewById(R.id.appbar_explore);
        tab = (TabLayout) view.findViewById(R.id.tab_explore);
        progress = (MaterialProgressBar) view.findViewById(R.id.progress);
        ivBookTypeRetry = (AppCompatImageView) view.findViewById(R.id.iv_book_type_retry);
        ivBookTypeMore = view.findViewById(R.id.iv_book_type_more);
        view.findViewById(R.id.view_search).setOnClickListener(this);
        view.findViewById(R.id.fl_action).setOnClickListener(this);
    }

    @Override
    public void onShow(boolean isFirstShow) {
        isShow = true;
        super.onShow(isFirstShow);
        if (isFirstShow && isBindPresenter) {
            getPresenter().start();
            getPresenter().loadData();
        }
    }

    private void setAdapter(final PagerAdapter adapter) {
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
        viewPager.setAdapter(adapter);
        CountEventHelper.countExploreTab(getContext(),
                adapter.getPageTitle(viewPager.getCurrentItem()).toString());
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                CountEventHelper.countExploreTab(getContext(),
                        adapter.getPageTitle(position).toString());
            }
        });
        tab.setupWithViewPager(viewPager);
    }

    @Override
    public void onBindPresenter(ExploreContract.Presenter presenter) {
        isBindPresenter = true;
        if (isShow) {
            presenter.start();
            presenter.loadData();
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            MobclickAgent.onPageEnd(getClass().getName());
        } else {
            MobclickAgent.onPageStart(getClass().getName());
        }
    }

    @Override
    public void showContent() {
        progress.setVisibility(View.GONE);
        ivBookTypeRetry.setVisibility(View.GONE);
        ivBookTypeMore.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        progress.setVisibility(View.VISIBLE);
        ivBookTypeRetry.setVisibility(View.GONE);
        ivBookTypeMore.setVisibility(View.GONE);
    }

    @Override
    public void showError(String errorMsg) {
        progress.setVisibility(View.GONE);
        ivBookTypeRetry.setVisibility(View.VISIBLE);
        ivBookTypeMore.setVisibility(View.GONE);
    }


    @NonNull
    @Override
    public ExploreContract.Presenter createPresenter() {
        return new ExplorePresenter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_search:
                AppRouter.showSearchActivity(ExploreFragment.this.getContext());
                break;
            case R.id.fl_action:
                if(ivBookTypeMore.getVisibility()==View.VISIBLE) {
                    getPresenter().openBookTypeSelection(getContext());
                }else if(ivBookTypeRetry.getVisibility()==View.VISIBLE){
                    getPresenter().loadData();
                }
                break;
        }
    }


    @Override
    public void setTabContent(@NonNull Fragment[] fragments, @NonNull String[] titles) {
        if(viewPager.getAdapter()==null) {
            BaseFragmentAdapter adapter = new BaseFragmentAdapter(getChildFragmentManager());
            adapter.setFragmentPages(fragments);
            adapter.setPageTitles(titles);
            setAdapter(adapter);
        }else{
            BaseFragmentAdapter adapter = (BaseFragmentAdapter) viewPager.getAdapter();
            adapter.setFragmentPages(fragments);
            adapter.setPageTitles(titles);
            adapter.notifyDataSetChanged();
        }
    }
}
