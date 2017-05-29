package com.youshibi.app.presentation.bookrack;

import android.support.annotation.NonNull;

import com.youshibi.app.R;
import com.youshibi.app.mvp.MvpFragment;

/**
 * Created by Chu on 2016/12/3.
 */

public class BookcaseFragment extends MvpFragment<BookcasePresenter> {

    public static BookcaseFragment newInstance() {


        BookcaseFragment fragment = new BookcaseFragment();
        return fragment;
    }

    @NonNull
    @Override
    public BookcasePresenter createPresenter() {
        return new BookcasePresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bookcase;
    }
}
