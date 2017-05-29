package com.youshibi.app.presentation.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youshibi.app.mvp.MvpActivity;

/**
 * Created by Chu on 2017/5/28.
 */

public class BookDetailActivity extends MvpActivity<BookDetailContract.Presenter> implements BookDetailContract.View {
    private static final String EXTRA_BOOK_ID = "book_id";

    public static Intent newIntent(Context context, String bookId) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(EXTRA_BOOK_ID, bookId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @NonNull
    @Override
    public BookDetailContract.Presenter createPresenter() {
        return new BookDetailPresenter();
    }

    @Override
    public void setListAdapter(BaseQuickAdapter adapter) {

    }
}
