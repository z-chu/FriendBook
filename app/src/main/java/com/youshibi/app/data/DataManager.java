package com.youshibi.app.data;


import com.google.gson.reflect.TypeToken;
import com.youshibi.app.AppContext;
import com.youshibi.app.BuildConfig;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.data.bean.BookSectionContent;
import com.youshibi.app.data.bean.BookSectionItem;
import com.youshibi.app.data.bean.BookType;
import com.youshibi.app.data.bean.DataList;
import com.youshibi.app.data.bean.AppRelease;
import com.youshibi.app.data.net.RequestClient;
import com.youshibi.app.rx.HttpResultFunc;
import com.zchu.rxcache.CacheTarget;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.diskconverter.GsonDiskConverter;
import com.zchu.rxcache.stategy.BaseStrategy;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zchu on 16-11-17.
 * <p>
 * 负责提供数据，这里采用了 Repository 模式，DataManager 就是一个仓库管理员，业务层 需要什么东西只需告诉仓库管理员，由仓库管理员把东西拿给它，并不需要知道东西实际放在哪。
 * 常见的数据来源有，RestAPI、SQLite数据库、本地缓存等
 * <p>
 * ps：有些时候，访问数据压根就涉及不到什么业务逻辑，如：请求数据给一个列表展示，这就压根没有业务逻辑
 * 这时present直接访问数据层就可以了，当然最好还是写一个业务逻辑类，什么也不干，只是转发一下数据，以后突然有业务逻辑了，就只要关注这个业务逻辑类就可以了
 */

public class DataManager {

    private static final DataManager sInstance = new DataManager();

    private RxCache rxCache;

    private DataManager() {
        rxCache = new RxCache.Builder()
                .appVersion(BuildConfig.VERSION_CODE)
                .diskDir(new File(AppContext.context().getFilesDir().getPath() + File.separator + "data-cache"))
                .setDebug(BuildConfig.DEBUG)
                .diskConverter(new GsonDiskConverter())//支持Serializable、Json(GsonDiskConverter)
                .memoryMax(0)
                .diskMax(50 * 1024 * 1024)
                .build();
    }

    public static DataManager getInstance() {
        return sInstance;
    }

    public Observable<DataList<Book>> getBookList(int page, int size) {
        return getBookList(0, page, size);
    }

    /**
     * 获取小说列表
     *
     * @param page     页码
     * @param size     每页的条目数
     * @param bookType 图书类别ID ，传0为获取全部
     */
    public Observable<DataList<Book>> getBookList(long bookType, int page, int size) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("page_index", page);
        hashMap.put("page_size", size);
        if (bookType > 0) hashMap.put("book_type", bookType);
        return RequestClient
                .getServerAPI()
                .getBookList(hashMap)
                .map(new HttpResultFunc<DataList<Book>>())
                .compose(rxCache.<DataList<Book>>transformer("getBookList" + page + size + bookType, new TypeToken<DataList<Book>>() {
                }.getType(), CacheStrategy.firstCache()))
                .map(new Func1<CacheResult<DataList<Book>>, DataList<Book>>() {
                    @Override
                    public DataList<Book> call(CacheResult<DataList<Book>> cacheResult) {
                        return cacheResult.getData();
                    }
                });
    }

    /**
     * 搜索，根据关键字获取小说列表
     *
     * @param page    页码
     * @param size    每页的条目数
     * @param keyword 搜索关键字
     */
    public Observable<DataList<Book>> searchBook(String keyword, int page, int size) {
        if (keyword == null || keyword.trim().length() == 0) {
            return Observable.just(new DataList<Book>());
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("page_index", page);
        hashMap.put("pageSize", size);
        hashMap.put("keyword", keyword);
        return RequestClient
                .getServerAPI()
                .searchBooks(hashMap)
                .map(new HttpResultFunc<DataList<Book>>());
    }

    /**
     * 获取小说类别
     */
    public Observable<CacheResult<List<BookType>>> getBookType() {
        return RequestClient
                .getServerAPI()
                .getBookType()
                .map(new HttpResultFunc<List<BookType>>())
                .compose(rxCache.<List<BookType>>transformer(
                        "getBookType",
                        new TypeToken<List<BookType>>() {
                        }.getType(),
                        new BaseStrategy() {
                            @Override
                            public <T> Observable<CacheResult<T>> execute(RxCache rxCache, String key, Observable<T> source, Type type) {
                                Observable<CacheResult<T>> cache = loadCache(rxCache, key, type);
                                Observable<CacheResult<T>> remote = loadRemote(rxCache, key, source, CacheTarget.MemoryAndDisk)
                                        .onErrorReturn(new Func1<Throwable, CacheResult<T>>() {
                                            @Override
                                            public CacheResult<T> call(Throwable throwable) {
                                                return null;
                                            }
                                        });
                                return Observable.concat(cache, remote)
                                        .filter(new Func1<CacheResult<T>, Boolean>() {
                                            @Override
                                            public Boolean call(CacheResult<T> result) {
                                                return result != null && result.getData() != null;
                                            }
                                        });
                            }
                        })
                );
    }


    /**
     * 获取小说章节列表
     *
     * @param bookId       小说的id
     * @param isOrderByAsc 是否升序排序
     */
    public Observable<List<BookSectionItem>> getBookSectionList(String bookId,
                                                                boolean isOrderByAsc) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (isOrderByAsc) {
            hashMap.put("order", "asc");
        } else {
            hashMap.put("order", "desc");
        }
        return RequestClient
                .getServerAPI()
                .getBookSectionList(bookId, hashMap)
                .map(new HttpResultFunc<List<BookSectionItem>>())
                .compose(rxCache.<List<BookSectionItem>>transformer("getBookSectionList" + bookId + isOrderByAsc, new TypeToken<List<BookSectionItem>>() {
                }.getType(), CacheStrategy.firstCache()))
                .map(new Func1<CacheResult<List<BookSectionItem>>, List<BookSectionItem>>() {
                    @Override
                    public List<BookSectionItem> call(CacheResult<List<BookSectionItem>> listCacheResult) {
                        return listCacheResult.getData();
                    }
                });
    }

    /**
     * 获取小说章节中的内容
     *
     * @param bookId       小说的id
     * @param sectionIndex 章节索引
     */
    public Observable<BookSectionContent> getBookSectionContent(String bookId, String sectionId, int sectionIndex) {
        return getBookSectionContent(bookId, sectionId, sectionIndex, "current");
    }

    public Observable<BookSectionContent> getBookSectionContentPrev(String bookId, String sectionId, int sectionIndex) {
        return getBookSectionContent(bookId, sectionId, sectionIndex, "last");
    }

    public Observable<BookSectionContent> getBookSectionContentNext(String bookId, String sectionId, int sectionIndex) {
        return getBookSectionContent(bookId, sectionId, sectionIndex, "next");
    }

    /**
     * 获取小说章节中的内容
     *
     * @param bookId       小说的id
     * @param sectionIndex 章节索引
     */
    public Observable<BookSectionContent> getBookSectionContent(String bookId, String sectionId, int sectionIndex, String direction) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("query_direction", direction);
        return RequestClient
                .getServerAPI()
                .getBookSectionContent(bookId, sectionIndex, hashMap)
                .map(new HttpResultFunc<BookSectionContent>())
                .compose(rxCache.<BookSectionContent>transformer("getBookSectionContent" + bookId + sectionId, BookSectionContent.class, CacheStrategy.firstCache()))
                .map(new Func1<CacheResult<BookSectionContent>, BookSectionContent>() {
                    @Override
                    public BookSectionContent call(CacheResult<BookSectionContent> bookSectionContentCacheResult) {
                        return bookSectionContentCacheResult.getData();
                    }
                });
    }


    public Observable<AppRelease> getLatestReleases() {
        return RequestClient
                .getServerAPI()
                .getLatestReleases()
                .map(new HttpResultFunc<AppRelease>());
    }

}
