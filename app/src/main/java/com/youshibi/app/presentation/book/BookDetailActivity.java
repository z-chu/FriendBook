package com.youshibi.app.presentation.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youshibi.app.R;
import com.youshibi.app.data.DBManger;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.mvp.MvpLoaderActivity;
import com.youshibi.app.ui.anim.InContentAnim;
import com.youshibi.app.ui.help.RecycleViewDivider;
import com.youshibi.app.ui.help.ToolbarHelper;
import com.youshibi.app.ui.widget.LoadErrorView;
import com.youshibi.app.ui.widget.ShapeTextView;
import com.youshibi.app.util.StringUtils;
import com.youshibi.app.util.ToastUtil;

/**
 * Created by Chu on 2017/5/28.
 */

public class BookDetailActivity extends MvpLoaderActivity<BookDetailContract.Presenter> implements BookDetailContract.View, View.OnClickListener {
    private static final String K_EXTRA_BOOK_ID = "book_id";
    private static final String K_EXTRA_BOOK = "book";

    private RecyclerView recyclerView;
    private LoadErrorView loadErrorView;

    private String bookId;
    private Book book;

    private ImageView ivCover;
    private TextView tvReadCount;
    private ShapeTextView tvIsFinish;
    private TextView tvAuthor;
    private TextView tvWordCount;


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
        ToolbarHelper.initToolbar(this, R.id.toolbar, true, book != null ? book.getName() : "书籍详情");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        loadErrorView = (LoadErrorView) findViewById(R.id.load_error_view);
        ivCover = (ImageView) findViewById(R.id.iv_cover);
        tvReadCount = (TextView) findViewById(R.id.tv_read_count);
        tvIsFinish = (ShapeTextView) findViewById(R.id.tv_is_finish);
        tvAuthor = (TextView) findViewById(R.id.tv_author);
        tvWordCount = (TextView) findViewById(R.id.tv_word_count);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        recyclerView.addItemDecoration(new RecycleViewDivider(this));
        bindOnClickLister(this, R.id.fl_add_bookcase, R.id.fl_open_book, R.id.fab);
        loadErrorView.setOnRetryListener(new LoadErrorView.OnRetryListener() {
            @Override
            public void onRetry(View view) {
                getPresenter().loadData();
            }
        });


        initDisplay();

    }

    private void initDisplay() {
        Glide
                .with(this)
                .load(book.getCoverUrl())
                .into(ivCover);
        tvReadCount.setText(StringUtils.formatCount(book.getClickNum())+"人读过");
        tvAuthor.setText(book.getBookTypeName()+" | "+book.getAuthor());
        tvIsFinish.setText(book.isFinished() ?
                getString(R.string.book_finished) : getString(R.string.book_unfinished));
        tvWordCount.setText(StringUtils.formatCount(book.getBookWordNum())+"字");

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

    @NonNull
    @Override
    public BookDetailContract.Presenter createPresenter() {
        return new BookDetailPresenter(book);
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
            case R.id.fl_open_book:
                if (recyclerView.getAdapter() != null) {
                    getPresenter().openRead(this, null,null);
                }
                break;
            case R.id.fl_add_bookcase:
                DBManger.getInstance().saveBookTb(book);
                ToastUtil.showToast("已加入书架");
                break;
            case R.id.fl_download_book:
                ToastUtil.showToast("该功能还未实现");
                break;
        }
    }
}
