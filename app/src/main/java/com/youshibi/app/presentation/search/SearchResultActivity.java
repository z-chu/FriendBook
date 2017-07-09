package com.youshibi.app.presentation.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.youshibi.app.AppRouter;
import com.youshibi.app.R;
import com.youshibi.app.base.BaseActivity;

/**
 * author : zchu
 * date   : 2017/7/5
 * desc   :
 */

public class SearchResultActivity extends BaseActivity implements View.OnClickListener {

    private static final String K_KEYWORD = "keyword";

    private ImageView ivArrowBack;
    private ImageView ivActionSearch;
    private EditText etSearch;

    private String mKeyword;

    public static Intent newIntent(Context context, @Nullable String keyword) {
        Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtra(K_KEYWORD, keyword);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        findView();
        bindOnClickLister(this, ivActionSearch, ivArrowBack,etSearch);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.ll_content_view,
                        SearchBookFragment.newInstance(getIntent().getStringExtra(K_KEYWORD))
                )
                .commit();
        mKeyword = getIntent().getStringExtra(K_KEYWORD);
        etSearch.setText(mKeyword);
        etSearch.setFocusable(false);

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
                break;
            case R.id.iv_action_search:
            case R.id.et_search:
                AppRouter.showSearchActivity(this,mKeyword);
                break;
        }
    }

}
