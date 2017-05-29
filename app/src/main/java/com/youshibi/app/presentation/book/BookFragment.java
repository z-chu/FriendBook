package com.youshibi.app.presentation.book;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.youshibi.app.base.BaseListFragment;
import com.youshibi.app.ui.help.RecycleViewDivider;

/**
 * Created by Chu on 2016/12/3.
 */

public class BookFragment extends BaseListFragment<BookPresenter> {

    private static final String BUNDLE_BOOK_TYPE = "book_type";

    public static BookFragment newInstance() {

        // Bundle args = new Bundle();

        BookFragment fragment = new BookFragment();
        //  fragment.setArguments(args);
        return fragment;
    }

    public static BookFragment newInstance(long bookType) {
        Bundle args = new Bundle();
        args.putLong(BUNDLE_BOOK_TYPE, bookType);
        BookFragment fragment = new BookFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setRecyclerView(RecyclerView recyclerView) {
        super.setRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity()));
    }

    @NonNull
    @Override
    public BookPresenter createPresenter() {
        Bundle arguments = getArguments();
        long bookType = 0;
        if (arguments != null) {
            bookType = arguments.getLong(BUNDLE_BOOK_TYPE);
        }
        return new BookPresenter(bookType);
    }


}
