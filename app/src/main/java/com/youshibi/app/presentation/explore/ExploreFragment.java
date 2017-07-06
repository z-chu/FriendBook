package com.youshibi.app.presentation.explore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.youshibi.app.AppRouter;
import com.youshibi.app.R;
import com.youshibi.app.base.BaseFragment;
import com.youshibi.app.data.DataManager;
import com.youshibi.app.data.bean.BookType;
import com.youshibi.app.presentation.book.BookFragment;
import com.youshibi.app.rx.SimpleSubscriber;
import com.youshibi.app.ui.help.BaseFragmentAdapter;
import com.youshibi.app.util.ToastUtil;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chu on 2017/5/28.
 */

public class ExploreFragment extends BaseFragment {

    private ViewPager viewPager;
    private AppBarLayout appbar;
 //   private Toolbar toolbar;
    private TabLayout tab;

    private Subscription mSubscribe;


    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_explore;
    }

    private void findView(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.vp_explore);
        appbar = (AppBarLayout) view.findViewById(R.id.appbar_explore);
       /* toolbar = (Toolbar) view.findViewById(R.id.toolbar_explore);*/
        tab = (TabLayout) view.findViewById(R.id.tab_explore);
        view.findViewById(R.id.view_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppRouter.showSearchActivity(ExploreFragment.this.getContext());
            }
        });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
       // toolbar.setTitle(getString(R.string.app_name));
    }

    @Override
    public void onShow(boolean isFirstShow) {
        super.onShow(isFirstShow);
        if (isFirstShow) {
            initViewPager();
        }
    }

    private void initViewPager() {
        mSubscribe = DataManager
                .getInstance()
                .getBookType()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<List<BookType>>() {

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtil.showToast(e.getMessage());

                    }

                    @Override
                    public void onNext(List<BookType> bookTypes) {
                        final BaseFragmentAdapter fragmentAdapter = new BaseFragmentAdapter(ExploreFragment.this.getChildFragmentManager());
                        Fragment[] fragments = new Fragment[bookTypes.size() + 1];
                        fragments[0] = BookFragment.newInstance();
                        String[] pageTitles = new String[bookTypes.size() + 1];
                        pageTitles[0] = "全部";
                        for (int i = 0; i < bookTypes.size(); i++) {
                            BookType bookType = bookTypes.get(i);
                            pageTitles[i + 1] = bookType.getTypeName();
                            fragments[i + 1] = BookFragment.newInstance(bookType.getId());
                        }
                        fragmentAdapter.setFragmentPages(fragments);
                        fragmentAdapter.setPageTitles(pageTitles);
                        setViewPage(fragmentAdapter);
                    }

                });
    }

    public void setViewPage(PagerAdapter adapter) {
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
        tab.setupWithViewPager(viewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscribe!=null&&!mSubscribe.isUnsubscribed()) {
            mSubscribe.unsubscribe();
        }
    }
}
