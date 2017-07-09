package com.youshibi.app.presentation.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youshibi.app.R;
import com.youshibi.app.base.BaseListFragment;
import com.youshibi.app.ui.help.RecycleViewDivider;

/**
 * Created by Chu on 2016/12/3.
 */

public class SearchBookFragment extends BaseListFragment<SearchBookPresenter> {

    private static final String BUNDLE_BOOK_KEYWORD = "book_keyword";

    public static SearchBookFragment newInstance(String keyword) {
        Bundle args = new Bundle();
        args.putString(BUNDLE_BOOK_KEYWORD, keyword);
        SearchBookFragment fragment = new SearchBookFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setRecyclerView(RecyclerView recyclerView) {
        super.setRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity()));
    }

    @Override
    public void setAdapter(BaseQuickAdapter adapter) {
        adapter.setEmptyView(R.layout.view_empty_search, recyclerView);
        super.setAdapter(adapter);
    }

    @NonNull
    @Override
    public SearchBookPresenter createPresenter() {
        return new SearchBookPresenter(getArguments().getString(BUNDLE_BOOK_KEYWORD));
    }


}
