package com.youshibi.app.presentation.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.youshibi.app.AppManager;
import com.youshibi.app.AppRouter;
import com.youshibi.app.R;
import com.youshibi.app.base.BaseActivity;
import com.youshibi.app.data.DBManger;
import com.youshibi.app.data.db.table.SearchHistory;
import com.youshibi.app.ui.help.RecyclerViewItemDecoration;
import com.youshibi.app.util.DensityUtil;
import com.youshibi.app.util.InputMethodUtils;

import java.util.List;


/**
 * author : zchu
 * date   : 2017/7/5
 * desc   :
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private static final String K_KEYWORD = "keyword";

    private ImageView ivArrowBack;
    private ImageView ivActionSearch;
    private ImageView ivActionClear;
    private EditText etSearch;
    private RecyclerView recyclerView;


    public static Intent newIntent(Context context, @Nullable String keyword) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(K_KEYWORD, keyword);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        findView();
        bindOnClickLister(this, ivActionSearch, ivArrowBack, ivActionClear);
        String keyword = getIntent().getStringExtra(K_KEYWORD);
        if (keyword != null) {
            etSearch.setText(keyword);
            etSearch.setSelection(keyword.length());
        }
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    goSearchResult();
                    return true;
                }
                return false;
            }
        });
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && etSearch.getText().length() > 0) {
                    ivActionClear.setVisibility(View.VISIBLE);
                } else {
                    ivActionClear.setVisibility(View.GONE);
                }
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0 && etSearch.isFocusable()) {
                    ivActionClear.setVisibility(View.VISIBLE);
                } else {
                    ivActionClear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etSearch.post(new Runnable() {
            @Override
            public void run() {
                InputMethodUtils.showSoftInput(etSearch);
            }
        });

        List<SearchHistory> searchHistories = DBManger.getInstance().loadSearchKeyword();
        if(searchHistories.size()>0) {
            LinearLayout linearLayout = findViewById(R.id.ll_content_view);
            recyclerView = new RecyclerView(this);
            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            linearLayout.addView(recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorForeground));
            recyclerView.addItemDecoration(new RecyclerViewItemDecoration.Builder(this)
                    .color(ContextCompat.getColor(this, R.color.colorDivider))
                    .thickness(1)
                    .create());
            final SearchHistoryAdapter searchHistoryAdapter = new SearchHistoryAdapter(searchHistories);
            TextView tvClearSearchHistory = new TextView(this);
            tvClearSearchHistory.setText(getString(R.string.clear_search_history));
            tvClearSearchHistory.setTextColor(ContextCompat.getColor(this, R.color.textPrimary));
            tvClearSearchHistory.setBackgroundResource(R.drawable.bg_list_item);
            tvClearSearchHistory
                    .setLayoutParams(
                            new RecyclerView.LayoutParams(
                                    RecyclerView.LayoutParams.MATCH_PARENT,
                                    DensityUtil.dp2px(this, 32)
                            )
                    );
            tvClearSearchHistory.setGravity(Gravity.CENTER);
            tvClearSearchHistory.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    searchHistoryAdapter.removeAllFooterView();
                    DBManger.getInstance().clearSearchKeyword();
                    searchHistoryAdapter.getData().clear();
                    searchHistoryAdapter.notifyDataSetChanged();
                }
            });
            searchHistoryAdapter.addFooterView(tvClearSearchHistory);
            recyclerView.setAdapter(searchHistoryAdapter);
        }

    }

    @Override
    protected void initImmersionBar(ImmersionBar immersionBar) {
        immersionBar
                .statusBarView(R.id.status_bar_view)
                .keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                .init();
    }

    @Override
    protected void onSlideStateChanged(int state) {

    }

    @Override
    public void onSlideCancel() {

    }

    private void findView() {
        ivArrowBack = (ImageView) findViewById(R.id.iv_arrow_back);
        ivActionSearch = (ImageView) findViewById(R.id.iv_action_search);
        ivActionClear = findViewById(R.id.iv_action_clear);
        etSearch = (EditText) findViewById(R.id.et_search);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_arrow_back:
                onBackPressed();
                InputMethodUtils.hideSoftInput(this);
                break;
            case R.id.iv_action_search:
                goSearchResult();
                break;
            case R.id.iv_action_clear:
                etSearch.setText("");
                break;
        }
    }

    private void goSearchResult() {
        Editable etSearchText = etSearch.getText();
        if (etSearchText != null) {
            String trim = etSearchText.toString().trim();
            if (trim.length() > 0) {
                DBManger.getInstance().saveSearchKeyword(trim);
                AppRouter.showSearchResultActivity(this, trim);
                AppManager.getInstance().finishActivity(getClass());
            }
        }
    }


    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, 0);
    }


}
