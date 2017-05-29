package com.youshibi.app.presentation.book;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;

import com.youshibi.app.base.BaseListContract;
import com.youshibi.app.base.BaseListFragment;
import com.youshibi.app.ui.widget.LoadErrorView;

/**
 * Created by Chu on 2016/12/3.
 */

public class BookFragment extends BaseListFragment<BookPresenter> implements SwipeRefreshLayout.OnRefreshListener, LoadErrorView.OnRetryListener, BaseListContract.View {

    private static final String BUNDLE_BOOK_TYPE="book_type";

    public static BookFragment newInstance() {

        // Bundle args = new Bundle();

        BookFragment fragment = new BookFragment();
        //  fragment.setArguments(args);
        return fragment;
    }

    public static BookFragment newInstance(long bookType) {
        Bundle args = new Bundle();
        args.putLong(BUNDLE_BOOK_TYPE,bookType);
        BookFragment fragment = new BookFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public BookPresenter createPresenter() {
        Bundle arguments = getArguments();
        long bookType=0;
        if(arguments!=null){
            bookType=arguments.getLong(BUNDLE_BOOK_TYPE);
        }
        return new BookPresenter(bookType);
    }



}
