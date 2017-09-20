package com.youshibi.app.presentation.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youshibi.app.R;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.mvp.MvpLoaderActivity;
import com.youshibi.app.ui.help.CommonAdapter;
import com.youshibi.app.ui.help.CommonViewHolder;
import com.youshibi.app.ui.help.RecyclerViewItemDecoration;
import com.youshibi.app.ui.help.ToolbarHelper;
import com.youshibi.app.util.DensityUtil;
import com.youshibi.app.util.ToastUtil;

import java.util.List;

/**
 * author : zchu
 * date   : 2017/9/18
 * desc   :
 */

public class BookCatalogActivity extends MvpLoaderActivity<BookCatalogContract.Presenter> implements BookCatalogContract.View {
    private static final String K_EXTRA_BOOK = "book";
    private static final String K_EXTRA_SECTION_COUNT = "section_count";
    private TextView tvSectionCount;
    private TextView tvSectionSelection;
    private LinearLayout llSectionSelection;
    private RecyclerView recyclerView;
    private BaseQuickAdapter sectionAdapter;
    private MaterialDialog dialog;
    private int sectionDataIndex;


    public static Intent newIntent(Context context, Book book, int sectionCount) {
        Intent intent = new Intent(context, BookCatalogActivity.class);
        intent.putExtra(K_EXTRA_BOOK, (Parcelable) book);
        intent.putExtra(K_EXTRA_SECTION_COUNT, sectionCount);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_catalog);
        ToolbarHelper.initToolbar(this, R.id.toolbar, true, "目录");
        initView();
        if (savedInstanceState != null) {
            sectionDataIndex = savedInstanceState.getInt("sectionDataIndex");
        }
        tvSectionCount.setText("共" + getIntent().getIntExtra(K_EXTRA_SECTION_COUNT, 50) + "章");
        llSectionSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sectionAdapter != null) {
                    showSectionSelectionDialog();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("sectionDataIndex", sectionDataIndex);
    }

    private void showSectionSelectionDialog() {
        if (dialog == null) {
            RecyclerView recyclerView = new RecyclerView(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new RecyclerViewItemDecoration.Builder(this)
                    .color(ContextCompat.getColor(this, R.color.colorDivider))
                    .thickness(1)
                    .create());
            recyclerView.setAdapter(sectionAdapter);
            dialog = new MaterialDialog.Builder(this)
                    .customView(recyclerView, true)
                    .build();
        } else {
            sectionAdapter.notifyDataSetChanged();
        }
        dialog.show();

    }

    @Override
    public void onBindPresenter(BookCatalogContract.Presenter presenter) {
        presenter.start();
    }

    @NonNull
    @Override
    public BookCatalogContract.Presenter createPresenter() {
        return new BookCatalogPresenter((Book) getIntent().getParcelableExtra(K_EXTRA_BOOK),
                getIntent().getIntExtra(K_EXTRA_SECTION_COUNT, 50));
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
    public void setListAdapter(BaseQuickAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration.Builder(this)
                .color(ContextCompat.getColor(this, R.color.colorDivider))
                .thickness(1)
                .create());
        adapter.bindToRecyclerView(recyclerView);
    }

    @Override
    public void setSectionData(List<String> sectionData) {
        this.sectionAdapter = createSectionAdapter(sectionData);
        if (TextUtils.isEmpty(tvSectionSelection.getText())) {
            tvSectionSelection.setText(sectionData.get(sectionDataIndex));
        }
    }

    private BaseQuickAdapter createSectionAdapter(List<String> sectionData) {
        BaseQuickAdapter adapter = new CommonAdapter<String>(sectionData) {

            @Override
            protected void convert(CommonViewHolder helper, String item) {
                TextView textView = ((TextView) helper.itemView);
                textView.setText(item);
                if (sectionDataIndex == helper.getLayoutPosition()) {
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                } else {
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.textSecondary));
                }
            }

            protected CommonViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
                TextView textView = new TextView(parent.getContext());
                textView.setLayoutParams(new ViewGroup
                        .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        DensityUtil.dp2px(parent.getContext(), 48)));
                textView.setGravity(Gravity.CENTER);
                return createBaseViewHolder(textView);
            }
        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                getPresenter().loadData(position + 1);
                sectionDataIndex = position;
                tvSectionSelection.setText((String) sectionAdapter.getData().get(position));
                ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(0, 0);
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        return adapter;
    }


    private void initView() {
        tvSectionCount = (TextView) findViewById(R.id.tv_section_count);
        tvSectionSelection = (TextView) findViewById(R.id.tv_section_selection);
        llSectionSelection = (LinearLayout) findViewById(R.id.ll_section_selection);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

    }

}
