package com.youshibi.app.presentation.read;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.TextView;

import com.youshibi.app.R;
import com.youshibi.app.data.bean.BookSectionContent;
import com.youshibi.app.mvp.MvpActivity;
import com.youshibi.app.ui.widget.LoadErrorView;

/**
 * Created by Chu on 2017/5/28.
 */

public class ReadActivity extends MvpActivity<ReadContract.Presenter> implements ReadContract.View {

    private static final String K_EXTRA_BOOK_ID = "book_id";
    private static final String K_EXTRA_SECTION_INDEX = "section_index";


    private NestedScrollView readView;
    private TextView tvReadSource;
    private LoadErrorView loadErrorView;

    private BookSectionContent mData;

    public static Intent newIntent(Context context, String bookId, int sectionIndex) {
        Intent intent = new Intent(context, ReadActivity.class);
        intent.putExtra(K_EXTRA_BOOK_ID, bookId);
        intent.putExtra(K_EXTRA_SECTION_INDEX, sectionIndex);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        loadErrorView = (LoadErrorView) findViewById(R.id.load_error_view);
        loadErrorView.setOnRetryListener(new LoadErrorView.OnRetryListener() {
            @Override
            public void onRetry(View view) {
                getPresenter().loadData();
            }
        });

        getPresenter().start();
        getPresenter().loadData();
    }

    @Override
    public void showContent() {
        loadErrorView.showContent();
        readView = (NestedScrollView) findViewById(R.id.read_view);
        readView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        tvReadSource = (TextView) findViewById(R.id.tv_read_source);
        tvReadSource.setText(mData.getContent());

    }

    @Override
    public void showLoading() {
        loadErrorView.makeLoading();

    }

    @Override
    public void showError(String errorMsg) {
        loadErrorView.makeError();
    }

    @NonNull
    @Override
    public ReadContract.Presenter createPresenter() {
        return new ReadPresenter(
                getIntent().getStringExtra(K_EXTRA_BOOK_ID),
                getIntent().getIntExtra(K_EXTRA_SECTION_INDEX, 0)
        );
    }

    @Override
    public void setData(BookSectionContent data) {
        this.mData=data;
    }
}
