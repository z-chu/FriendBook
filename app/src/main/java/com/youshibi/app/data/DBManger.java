package com.youshibi.app.data;

import android.support.annotation.Nullable;

import com.youshibi.app.AppContext;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.data.db.DBRepository;
import com.youshibi.app.data.db.table.BookTb;
import com.youshibi.app.data.db.table.BookTbDao;
import com.youshibi.app.data.db.table.DaoSession;
import com.youshibi.app.data.db.table.SearchHistory;
import com.youshibi.app.data.db.table.SearchHistoryDao;
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
            bookTb.setSort((int) mDaoSession.getBookTbDao().count());
            mDaoSession.getBookTbDao().insert(bookTb);
            RxBus.getDefault().post(new AddBook2BookcaseEvent(bookTb));
        } else {
            bookTb = DataConvertUtil.book2BookTb(book, bookTb);
            mDaoSession.getBookTbDao().update(bookTb);
        }
        return book.getId();
    }

    /**
     * 添加图书到书架
     *
     * @return book 在数据库中的Id
     */
    public String saveBookTb(BookTb bookTb) {
        if (loadBookTbById(bookTb.getId()) == null) {
            bookTb.setSort((int) mDaoSession.getBookTbDao().count());
            mDaoSession.getBookTbDao().insert(bookTb);
            RxBus.getDefault().post(new AddBook2BookcaseEvent(bookTb));
        } else {
            mDaoSession.getBookTbDao().update(bookTb);
        }
        return bookTb.getId();
    }

    public void deleteBookTb(BookTb bookTb) {
        mDaoSession.getBookTbDao().delete(bookTb);
    }

    public void deleteBookTbs(Iterable<BookTb> entities) {
        mDaoSession.getBookTbDao().deleteInTx(entities);
    }

    public boolean updateBookTb(BookTb bookTb) {
        if (loadBookTbById(bookTb.getId()) != null) {
            mDaoSession.getBookTbDao().update(bookTb);
            return true;
        }
        return false;
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

    public long getBookTbCount() {
        return mDaoSession.getBookTbDao().count();
    }

    public Observable<List<BookTb>> loadBookTb() {
        return mDaoSession
                .getBookTbDao()
                .queryBuilder()
                .orderAsc(BookTbDao.Properties.Sort)
                .rx()
                .list();
    }

    public Observable<List<BookTb>> loadBookTbOrderLatestRead() {
        return mDaoSession
                .getBookTbDao()
                .queryBuilder()
                .orderDesc(BookTbDao.Properties.LatestReadTimestamp, BookTbDao.Properties.Sort)
                .rx()
                .list();
    }

    public Observable<List<BookTb>> loadBookTbOrderMostRead() {
        return mDaoSession
                .getBookTbDao()
                .queryBuilder()
                .orderDesc(BookTbDao.Properties.ReadNumber, BookTbDao.Properties.Sort)
                .rx()
                .list();
    }

    public Observable<List<BookTb>> loadBookTbOrderName() {
        return mDaoSession
                .getBookTbDao()
                .queryBuilder()
                .orderAsc(BookTbDao.Properties.Name, BookTbDao.Properties.Sort)
                .rx()
                .list();
    }

    public void clearBookTbSort() {
        List<BookTb> bookTbs = mDaoSession
                .getBookTbDao()
                .loadAll();
        for (BookTb bookTb : bookTbs) {
            bookTb.setSort(0);
        }
        mDaoSession.getBookTbDao().updateInTx(bookTbs);
    }

    public void updateBookTbSort(List<BookTb> bookTbs) {
        for (int i = 0; i < bookTbs.size(); i++) {
            BookTb bookTb = bookTbs.get(i);
            bookTb.setSort(i);
        }
        mDaoSession.getBookTbDao().updateInTx(bookTbs);
    }


    public void saveSearchKeyword(@Nullable String keyword) {
        if (keyword != null) {
            SearchHistoryDao searchHistoryDao = mDaoSession.getSearchHistoryDao();
            SearchHistory searchHistory = searchHistoryDao.load(keyword);
            if (searchHistory != null) {
                searchHistory.setTimestamp(System.currentTimeMillis());
                searchHistoryDao.update(searchHistory);
            } else {
                searchHistory = new SearchHistory();
                searchHistory.setKeyword(keyword);
                searchHistory.setTimestamp(System.currentTimeMillis());
                searchHistoryDao.insert(searchHistory);
            }
        }
    }

    public void deleteSearchKeyword(@Nullable String keyword) {
        if (keyword != null) {
            SearchHistoryDao searchHistoryDao = mDaoSession.getSearchHistoryDao();
            searchHistoryDao.deleteByKey(keyword);
        }
    }

    public void clearSearchKeyword() {
        SearchHistoryDao searchHistoryDao = mDaoSession.getSearchHistoryDao();
        searchHistoryDao.deleteAll();
    }

    public List<SearchHistory> loadSearchKeyword() {
        SearchHistoryDao searchHistoryDao = mDaoSession.getSearchHistoryDao();
        return searchHistoryDao
                .queryBuilder()
                .orderDesc(SearchHistoryDao.Properties.Timestamp)
                .list();
    }
}
