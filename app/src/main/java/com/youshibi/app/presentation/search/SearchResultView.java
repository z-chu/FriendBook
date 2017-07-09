package com.youshibi.app.presentation.search;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youshibi.app.base.BaseLceView;

/**
 * Created by Chu on 2017/7/9.
 */

public interface SearchResultView extends BaseLceView {
    void setListAdapter(BaseQuickAdapter adapter);
}
