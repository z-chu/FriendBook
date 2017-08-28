package com.youshibi.app.presentation.book;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.youshibi.app.R;
import com.youshibi.app.base.BaseListFragment;
import com.youshibi.app.ui.help.RecyclerViewItemDecoration;
import com.youshibi.app.ui.widget.LoadErrorView;

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadErrorView.setErrorViewCreatedListener(new LoadErrorView.OnViewCreatedListener() {
            @Override
            public void onViewCreated(@NonNull View view) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                layoutParams.topMargin=-getResources().getDimensionPixelSize(R.dimen.bottom_navigation_height);
                view.requestLayout();
            }
        });
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
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration.Builder(getActivity())
                .color(ContextCompat.getColor(getActivity(), R.color.colorDivider))
                .thickness(1)
                .create());
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
