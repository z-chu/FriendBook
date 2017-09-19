package com.youshibi.app.presentation.book;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youshibi.app.base.BaseLceView;
import com.youshibi.app.mvp.MvpPresenter;

import java.util.List;

/**
 * Created by Chu on 2017/5/28.
 */

public interface BookCatalogContract {

    interface View extends BaseLceView {
        void setListAdapter(BaseQuickAdapter adapter);

        void setSectionData(List<String> sectionData);

    }

    interface Presenter extends MvpPresenter<View> {

        void loadData(int page);
    }
}
