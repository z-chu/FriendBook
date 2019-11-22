package com.youshibi.app.presentation.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youshibi.app.AppRouter;
import com.youshibi.app.R;
import com.youshibi.app.data.DBManger;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.data.bean.BookDetail;
import com.youshibi.app.mvp.MvpLoaderActivity;
import com.youshibi.app.ui.help.CommonAdapter;
import com.youshibi.app.ui.help.CommonViewHolder;
import com.youshibi.app.ui.help.ToolbarHelper;
import com.youshibi.app.ui.widget.ShapeTextView;
import com.youshibi.app.util.DateUtil;
import com.youshibi.app.util.GlideApp;
import com.youshibi.app.util.StringUtils;
import com.youshibi.app.util.ToastUtil;

import java.util.List;

/**
 * Created by Chu on 2017/5/28.
 */

public class BookDetailActivity extends MvpLoaderActivity<BookDetailContract.Presenter> implements BookDetailContract.View, View.OnClickListener {
    private static final String K_EXTRA_BOOK = "book";


    private Book book;
    private BookDetail bookDetail;

    private ImageView ivCover;
    private TextView tvReadCount;
    private ShapeTextView tvIsFinish;
    private TextView tvAuthor;
    private TextView tvWordCount;
    private TextView tvCatalogTitle;
    private TextView tvUpdateTime;
    private TextView tvDescribe;
    private RecyclerView rvRecommendBook;
    private TextView tvWordCountCopyright;
    private TextView tvCreateDateCopyright;





    public static Intent newIntent(Context context, Book book) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(K_EXTRA_BOOK, (Parcelable) book);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initExtra();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ToolbarHelper.initToolbar(this, R.id.toolbar, true, book != null ? book.getName() : "书籍详情");

        ivCover = (ImageView) findViewById(R.id.iv_cover);
        tvReadCount = (TextView) findViewById(R.id.tv_read_count);
        tvIsFinish = (ShapeTextView) findViewById(R.id.tv_is_finish);
        tvAuthor = (TextView) findViewById(R.id.tv_author);
        tvWordCount = (TextView) findViewById(R.id.tv_word_count);
        tvCatalogTitle = (TextView) findViewById(R.id.tv_catalog_title);
        tvDescribe = (TextView) findViewById(R.id.tv_describe);
        tvWordCountCopyright = (TextView) findViewById(R.id.tv_word_count_copyright);
        tvCreateDateCopyright = (TextView) findViewById(R.id.tv_create_date_copyright);
        tvUpdateTime = findViewById(R.id.tv_update_time);
        rvRecommendBook = findViewById(R.id.rv_recommend_book);
        rvRecommendBook.setNestedScrollingEnabled(false);


//        Context context = ;
//        Toast.makeText(context);
        bindOnClickLister(this, R.id.fl_add_bookcase,R.id.fl_download_book, R.id.fl_open_book, R.id.ll_book_detail_catalog);
        initDisplay();

    }

    private void initDisplay() {
        GlideApp
                .with(this)
                .load(book.getCoverUrl())
                .placeholder(R.drawable.ic_book_cover_default)
                .into(ivCover);
        tvReadCount.setText(StringUtils.formatCount(book.getClickNum()) + "人读过");
        tvAuthor.setText(book.getBookTypeName() + " | " + book.getAuthor());
        tvIsFinish.setText(book.isFinished() ?
                getString(R.string.book_finished) : getString(R.string.book_unfinished));
        tvWordCount.setText(StringUtils.formatCount(book.getBookWordNum()) + "字");
        tvWordCountCopyright.setText(tvWordCountCopyright.getText()+StringUtils.formatCount(book.getBookWordNum()) + "字");
        tvCreateDateCopyright.setText(tvCreateDateCopyright.getText()+book.getCreateDateTime());
        tvDescribe.setText(book.getDescribe());


    }

    @Override
    public void onBindPresenter(BookDetailContract.Presenter presenter) {
        presenter.start();
        presenter.loadData();
    }

    private void initExtra() {
        book = getIntent().getParcelableExtra(K_EXTRA_BOOK);
    }

    @NonNull
    @Override
    public BookDetailContract.Presenter createPresenter() {
        return new BookDetailPresenter(book);
    }

    @Override
    public void setListAdapter(BaseQuickAdapter adapter) {
    }

    @Override
    public void setRecommendBooks(List<Book> books) {
        rvRecommendBook.setLayoutManager(new GridLayoutManager(this, 3));
        rvRecommendBook.setNestedScrollingEnabled(false);
        CommonAdapter<Book> commonAdapter = new CommonAdapter<Book>(R.layout.grid_item_bookcase_book, books) {
            @Override
            protected void convert(CommonViewHolder helper, Book item) {
                GlideApp
                        .with(mContext)
                        .load(item.getCoverUrl())
                        .placeholder(R.drawable.ic_book_cover_default)
                        .into((ImageView) helper.getView(R.id.iv_cover));
                helper.setText(R.id.tv_title, item.getName());
                helper.addOnClickListener(R.id.iv_cover);
            }
        };
        commonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AppRouter.showBookDetailActivity(view.getContext(), (Book) adapter.getData().get(position));
            }
        });
        commonAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                AppRouter.showBookDetailActivity(view.getContext(), (Book) adapter.getData().get(position));
            }
        });
        rvRecommendBook.setAdapter(commonAdapter);


    }

    @Override
    public void showContent() {
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showError(String errorMsg) {
        ToastUtil.showToast(errorMsg);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_open_book:
                getPresenter().openRead(this);
                break;
            case R.id.fl_add_bookcase:
                DBManger.getInstance().saveBookTb(book);
                ToastUtil.showToast("已加入书架");
                break;
            case R.id.fl_download_book:
                ToastUtil.showToast(getString(R.string.developing));
                break;
            case R.id.ll_book_detail_catalog:
                if (bookDetail != null) {
                    AppRouter.showBookCatalogActivity(this, book, bookDetail.getChapterCount());
                }
                break;
        }
    }

    @Override
    public void setData(BookDetail data) {
        this.book = data.getBook();
        this.bookDetail = data;
        if (book.isFinished()) {
            tvCatalogTitle.setText("查看目录：共" + data.getChapterCount() + "章");
            tvUpdateTime.setText("已完结");
        } else {
            tvCatalogTitle.setText("最新章节：" + data.getLatestChapter().getChapterName());
            long aLong = Long.parseLong(data.getLatestChapter().getCreateTime() + "000");
            tvUpdateTime.setText(DateUtil.formatSomeAgo(aLong));
        }
    }
}
