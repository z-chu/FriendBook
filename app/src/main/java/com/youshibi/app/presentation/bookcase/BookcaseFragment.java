package com.youshibi.app.presentation.bookcase;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.youshibi.app.base.BaseListFragment;
import com.youshibi.app.ui.help.RecycleViewDivider;

/**
 * Created by Chu on 2016/12/3.
 */

public class BookcaseFragment extends BaseListFragment<BookcasePresenter> {

    public static BookcaseFragment newInstance() {
        return new BookcaseFragment();
    }

    @Override
    public void setRecyclerView(RecyclerView recyclerView) {
        super.setRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity()));
    }

    @NonNull
    @Override
    public BookcasePresenter createPresenter() {
        return new BookcasePresenter();
    }

}
