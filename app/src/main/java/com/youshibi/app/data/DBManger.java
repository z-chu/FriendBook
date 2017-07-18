package com.youshibi.app.data;

import com.youshibi.app.AppContext;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.data.db.DBRepository;
import com.youshibi.app.data.db.table.BookTb;
import com.youshibi.app.data.db.table.DaoSession;
import com.youshibi.app.event.AddBook2BookcaseEvent;
import com.youshibi.app.rx.RxBus;
import com.youshibi.app.util.DataConvertUtil;

import java.util.List;

import rx.Observable;

/**
 * Created by Chu on 2017/5/29.
 */

public final class DBManger {

    private static final DBManger sDBManger = new DBManger();

    private DaoSession mDaoSession;

    private DBManger() {
        DBRepository.initDatabase(AppContext.context());
        mDaoSession = DBRepository.getDaoSession();
    }

    public static DBManger getInstance() {
        return sDBManger;
    }

    /**
     * 添加图书到书架
     *
     * @return book 在数据库中的Id
     */
    public String saveBookTb(Book book) {
        BookTb bookTb = loadBookTbById(book.getId());
        if (bookTb == null) {
            bookTb = DataConvertUtil.book2BookTb(book, null);
            mDaoSession.getBookTbDao().insert(bookTb);
        } else {
            bookTb = DataConvertUtil.book2BookTb(book, bookTb);
            mDaoSession.getBookTbDao().update(bookTb);
        }
        RxBus.getDefault().post(new AddBook2BookcaseEvent(bookTb));
        return book.getId();
    }



    public boolean hasBookTb(String bookId) {
        return loadBookTbById(bookId) != null;
    }

    public boolean hasBookTb(Book book) {
        return hasBookTb(book.getId());
    }

    public BookTb loadBookTbById(String bookId) {
        return mDaoSession
                .getBookTbDao()
                .load(bookId);
    }

    public long getBookTbCount(){
        return mDaoSession.getBookTbDao().count();
    }

    public Observable<List<BookTb>> loadBookTb(){
        return mDaoSession
                .getBookTbDao()
                .rx()
                .loadAll();
    }


}
