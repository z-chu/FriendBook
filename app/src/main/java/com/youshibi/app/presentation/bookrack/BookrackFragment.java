package com.youshibi.app.presentation.bookrack;

import android.support.annotation.NonNull;

import com.youshibi.app.mvp.MvpFragment;

/**
 * Created by Chu on 2016/12/3.
 */

public class BookrackFragment extends MvpFragment<BookrackPresenter> {
    @NonNull
    @Override
    public BookrackPresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return 0;
    }
}
