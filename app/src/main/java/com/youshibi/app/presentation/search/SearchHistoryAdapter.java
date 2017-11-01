package com.youshibi.app.presentation.search;

import android.view.View;

import com.youshibi.app.AppManager;
import com.youshibi.app.AppRouter;
import com.youshibi.app.R;
import com.youshibi.app.data.DBManger;
import com.youshibi.app.data.db.table.SearchHistory;
import com.youshibi.app.ui.help.CommonAdapter;
import com.youshibi.app.ui.help.CommonViewHolder;

import java.util.List;

/**
 * author : zchu
 * date   : 2017/11/1
 * desc   :
 */

public class SearchHistoryAdapter extends CommonAdapter<SearchHistory> {

    public SearchHistoryAdapter(List<SearchHistory> data) {
        super(R.layout.list_item_search_history, data);
    }

    @Override
    protected void convert(final CommonViewHolder helper, final SearchHistory item) {
        helper.setText(R.id.tv_keyword, item.getKeyword());
        helper.setOnClickListener(R.id.iv_keyword_clear, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBManger.getInstance().deleteSearchKeyword(item.getKeyword());
                if(mData.size()>1) {
                    SearchHistoryAdapter.this.remove(helper.getLayoutPosition());
                }else{
                    SearchHistoryAdapter.this.removeAllFooterView();
                    mData.clear();
                    SearchHistoryAdapter.this.notifyDataSetChanged();
                }

            }
        });
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBManger.getInstance().saveSearchKeyword(item.getKeyword());
                AppRouter.showSearchResultActivity(mContext, item.getKeyword());
                AppManager.getInstance().finishActivity(SearchActivity.class);
            }
        });
    }
}
