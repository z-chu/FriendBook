package com.youshibi.app.presentation.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youshibi.app.R;
import com.youshibi.app.mvp.MvpActivity;
import com.youshibi.app.ui.anim.InContentAnim;
import com.youshibi.app.ui.help.RecycleViewDivider;
import com.youshibi.app.ui.widget.LoadErrorView;
import com.youshibi.app.util.ToastUtil;

/**
 * Created by Chu on 2017/5/28.
 */

public class BookDetailActivity extends MvpActivity<BookDetailContract.Presenter> implements BookDetailContract.View {
    private static final String K_EXTRA_BOOK_ID = "book_id";

    private RecyclerView recyclerView;
    private LoadErrorView loadErrorView;


    public static Intent newIntent(Context context, String bookId) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(K_EXTRA_BOOK_ID, bookId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        loadErrorView = (LoadErrorView) findViewById(R.id.load_error_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        recyclerView.addItemDecoration(new RecycleViewDivider(this));
        loadErrorView.setOnRetryListener(new LoadErrorView.OnRetryListener() {
            @Override
            public void onRetry(View view) {
                getPresenter().loadData();
            }
        });
        getPresenter().start();
        getPresenter().loadData();

    }

    @NonNull
    @Override
    public BookDetailContract.Presenter createPresenter() {
        return new BookDetailPresenter(getIntent().getStringExtra(K_EXTRA_BOOK_ID));
    }

    @Override
    public void setListAdapter(BaseQuickAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showContent() {
        new InContentAnim(recyclerView, loadErrorView).start();
    }

    @Override
    public void showLoading() {
        recyclerView.setVisibility(View.GONE);
        loadErrorView.makeLoading();

    }

    @Override
    public void showError(String errorMsg) {
        ToastUtil.showToast(errorMsg);
        recyclerView.setVisibility(View.GONE);
        loadErrorView.makeError();

    }
}
