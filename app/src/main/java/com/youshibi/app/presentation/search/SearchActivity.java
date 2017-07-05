package com.youshibi.app.presentation.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.youshibi.app.R;
import com.youshibi.app.base.BaseActivity;
import com.youshibi.app.ui.help.ToolbarHelper;

/**
 * author : zchu
 * date   : 2017/7/5
 * desc   :
 */

public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
//        Toolbar toolbar = ToolbarHelper.initToolbar(this, R.id.toolbar, true,null);
       // toolbar.inflateMenu(R.menu.main);
    }


}
