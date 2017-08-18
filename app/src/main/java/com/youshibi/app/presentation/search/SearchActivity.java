package com.youshibi.app.presentation.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.youshibi.app.AppManager;
import com.youshibi.app.AppRouter;
import com.youshibi.app.R;
import com.youshibi.app.base.BaseActivity;
import com.youshibi.app.util.InputMethodUtils;


/**
 * author : zchu
 * date   : 2017/7/5
 * desc   :
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private static final String K_KEYWORD = "keyword";

    private ImageView ivArrowBack;
    private ImageView ivActionSearch;
    private EditText etSearch;

    private Handler mHandler = new Handler();

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
        bindOnClickLister(this, ivActionSearch, ivArrowBack);
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
        etSearch.post(new Runnable() {
            @Override
            public void run() {
                InputMethodUtils.showSoftInput(etSearch);
            }
        });
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
        }
    }

    private void goSearchResult() {
        Editable etSearchText = etSearch.getText();
        if (etSearchText != null) {
            String trim = etSearchText.toString().trim();
            if (trim.length() > 0) {
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
