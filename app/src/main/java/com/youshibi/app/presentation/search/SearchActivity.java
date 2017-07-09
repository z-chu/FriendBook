package com.youshibi.app.presentation.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youshibi.app.R;
import com.youshibi.app.mvp.MvpActivity;
import com.youshibi.app.util.ToastUtil;

/**
 * author : zchu
 * date   : 2017/7/5
 * desc   :
 */

public class SearchActivity extends MvpActivity<SearchContract.Presenter> implements SearchContract.View, View.OnClickListener {

    private ImageView ivArrowBack;
    private ImageView ivActionSearch;
    private EditText etSearch;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        findView();
        bindOnClickLister(this, ivActionSearch, ivArrowBack);

        getPresenter().start();
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
                getPresenter().search(etSearch.getText().toString().trim());
                break;
        }
    }

    @NonNull
    @Override
    public SearchContract.Presenter createPresenter() {
        return new SearchPresenter();
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

    }
}
