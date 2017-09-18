package com.youshibi.app.presentation.book;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.youshibi.app.R;
import com.youshibi.app.base.BaseActivity;

/**
 * author : zchu
 * date   : 2017/9/18
 * desc   :
 */

public class BookCatalogActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_catalog);
    }
}
