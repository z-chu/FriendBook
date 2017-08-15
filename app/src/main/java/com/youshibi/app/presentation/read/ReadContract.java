package com.youshibi.app.presentation.read;

import com.youshibi.app.base.BaseLceView;
import com.youshibi.app.data.bean.BookSectionContent;
import com.youshibi.app.mvp.MvpPresenter;
import com.zchu.reader.OnPageChangeListener;
import com.zchu.reader.PageLoaderAdapter;

/**
 * Created by Chu on 2017/5/28.
 */

public interface ReadContract {

    interface View extends BaseLceView {

        void setData(BookSectionContent data);

        void setPageAdapter(PageLoaderAdapter adapter);

        void  openSection(int section);

    }

    interface Presenter extends MvpPresenter<View>, OnPageChangeListener {


        void loadData();

    }
}
