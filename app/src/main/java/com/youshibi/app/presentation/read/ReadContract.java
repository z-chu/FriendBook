package com.youshibi.app.presentation.read;

import com.youshibi.app.base.BaseLceView;
import com.youshibi.app.mvp.MvpPresenter;
import com.zchu.reader.OnPageChangeListener;
import com.zchu.reader.PageLoaderAdapter;

/**
 * Created by Chu on 2017/5/28.
 */

public interface ReadContract {

    interface View extends BaseLceView {

        void setPageAdapter(PageLoaderAdapter adapter);

        void setSectionListAdapter(BookSectionAdapter adapter);

        void openSection(int section, int page);

        void setSectionDisplay(String sectionName,int section);

    }

    interface Presenter extends MvpPresenter<View>, OnPageChangeListener {

        void loadData();

        /**
         * 保存阅读位置
         */
        void saveReadLocation();

        void nextSection();

        void openSection(int pos);

        void prevSection();

    }
}
