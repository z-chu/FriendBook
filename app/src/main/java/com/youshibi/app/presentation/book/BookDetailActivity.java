package com.youshibi.app.presentation.book;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youshibi.app.R;
import com.youshibi.app.data.DBManger;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.mvp.MvpLoaderActivity;
import com.youshibi.app.ui.anim.InContentAnim;
import com.youshibi.app.ui.help.RecycleViewDivider;
import com.youshibi.app.ui.help.ToolbarHelper;
import com.youshibi.app.ui.widget.LoadErrorView;
import com.youshibi.app.util.ToastUtil;

/**
 * Created by Chu on 2017/5/28.
 */

public class BookDetailActivity extends MvpLoaderActivity<BookDetailContract.Presenter> implements BookDetailContract.View {
    private static final String K_EXTRA_BOOK_ID = "book_id";
    private static final String K_EXTRA_BOOK = "book";

    private RecyclerView recyclerView;
    private LoadErrorView loadErrorView;

    private String bookId;
    private Book book;

    private boolean isShowCollectionDialog = false;

    public static Intent newIntent(Context context, String bookId) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(K_EXTRA_BOOK_ID, bookId);
        return intent;
    }

    public static Intent newIntent(Context context, Book book) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(K_EXTRA_BOOK, (Parcelable) book);
        intent.putExtra(K_EXTRA_BOOK_ID, book.getId());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initExtra();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ToolbarHelper.initToolbar(this,R.id.toolbar,true,book!=null?book.getName():"书籍详情");
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


    }

    @Override
    public void onBindPresenter(BookDetailContract.Presenter presenter) {
        presenter.start();
        presenter.loadData();
    }

    private void initExtra() {
        bookId = getIntent().getStringExtra(K_EXTRA_BOOK_ID);
        book = getIntent().getParcelableExtra(K_EXTRA_BOOK);
    }

    @Override
    public void onBackPressed() {

        if (isShowCollectionDialog||DBManger.getInstance().hasBookTb(bookId)) {
            //书架已经有这本书了
            super.onBackPressed();
        } else {
            //书架没有这本书了
            if (book == null) {
                super.onBackPressed();
            } else {
                showCollectionDialog();

            }


        }
    }

    private void showCollectionDialog() {
        new MaterialDialog
                .Builder(this)
                .title("加入书架")
                .content("是否将《" + book.getName() + "》加入书架")
                .positiveText("加入")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                       DBManger.getInstance().saveBookTb(book);
                        ToastUtil.showToast("已加入书架");
                    }
                })
                .negativeText("取消")
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        BookDetailActivity.this.finish();
                    }
                })
                .show();
        isShowCollectionDialog = true;
    }

    @NonNull
    @Override
    public BookDetailContract.Presenter createPresenter() {
        return new BookDetailPresenter(bookId);
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
        loadErrorView.showLoading();

    }

    @Override
    public void showError(String errorMsg) {
        ToastUtil.showToast(errorMsg);
        recyclerView.setVisibility(View.GONE);
        loadErrorView.showError();

    }
}
