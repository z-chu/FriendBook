package com.youshibi.app.presentation.book;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youshibi.app.base.BaseLcedView;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.data.bean.BookDetail;
import com.youshibi.app.mvp.MvpPresenter;

import java.util.List;

/**
 * Created by Chu on 2017/5/28.
 */

public interface BookDetailContract {

    interface View extends BaseLcedView<BookDetail> {

        void setListAdapter(BaseQuickAdapter adapter);

        void setRecommendBooks(List<Book> books);


    }

    interface Presenter extends MvpPresenter<View> {
        void loadData();

        void openRead(Context context);
    }
}
