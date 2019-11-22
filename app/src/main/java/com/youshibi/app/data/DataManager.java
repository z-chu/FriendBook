package com.youshibi.app.data;


import com.google.gson.reflect.TypeToken;
import com.youshibi.app.AppContext;
import com.youshibi.app.BuildConfig;
import com.youshibi.app.data.bean.AppRelease;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.data.bean.BookChapter;
import com.youshibi.app.data.bean.BookDetail;
import com.youshibi.app.data.bean.BookChapterContent;
import com.youshibi.app.data.bean.BookType;
import com.youshibi.app.data.bean.Channel;
import com.youshibi.app.data.bean.DataList;
import com.youshibi.app.data.bean.LatestChapter;
import com.youshibi.app.data.net.RequestClient;
import com.youshibi.app.rx.HttpResultFunc;
import com.zchu.rxcache.CacheTarget;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.diskconverter.GsonDiskConverter;
import com.zchu.rxcache.stategy.BaseStrategy;
import com.zchu.rxcache.stategy.CacheStrategy;
import com.zchu.rxcache.stategy.IStrategy;

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
        hashMap.put("page_size", size);
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
    public Observable<List<BookChapter>> getBookSectionList(String bookId,
                                                            boolean isOrderByAsc) {
        return getBookSectionList(bookId, isOrderByAsc, null, null);
    }

    /**
     * 获取小说章节列表
     *
     * @param bookId       小说的id
     * @param isOrderByAsc 是否升序排序
     */
    public Observable<List<BookChapter>> getBookSectionList(String bookId,
                                                                boolean isOrderByAsc, Integer page, Integer size) {
        return getBookSectionList(bookId, isOrderByAsc, page, size, false);
    }

    public Observable<List<BookChapter>> getBookSectionList(String bookId,
                                                                boolean isOrderByAsc,
                                                                Integer page,
                                                                Integer size,
                                                                boolean updated) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (isOrderByAsc) {
            hashMap.put("order", "asc");
        } else {
            hashMap.put("order", "desc");
        }
        if (page != null) {
            hashMap.put("page_index", page);
        }
        if (size != null) {
            hashMap.put("page_size", size);
        }
        IStrategy iStrategy;
        if (updated) {
            iStrategy = CacheStrategy.firstRemote();
        } else {
            iStrategy = CacheStrategy.firstCache();
        }
        return RequestClient
                .getServerAPI()
                .getBookSectionList(bookId, hashMap)
                .map(new HttpResultFunc<List<BookChapter>>())
                .compose(rxCache.<List<BookChapter>>transformer("getBookSectionList" + bookId + isOrderByAsc + page + size,
                        new TypeToken<List<BookChapter>>() {
                        }.getType(), iStrategy))
                .map(new Func1<CacheResult<List<BookChapter>>, List<BookChapter>>() {
                    @Override
                    public List<BookChapter> call(CacheResult<List<BookChapter>> listCacheResult) {
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
    public Observable<BookChapterContent> getBookSectionContent(String bookId, String sectionId, int sectionIndex) {
        return getBookSectionContent(bookId, sectionId, sectionIndex, "current");
    }

    /**
     * 获取小说章节中的内容
     *
     * @param bookId       小说的id
     * @param sectionIndex 章节索引
     */
    public Observable<BookChapterContent> getBookSectionContent(String bookId, String sectionId, int sectionIndex, String direction) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("query_direction", direction);
        return RequestClient
                .getServerAPI()
                .getBookSectionContent(bookId, sectionIndex, hashMap)
                .map(new HttpResultFunc<BookChapterContent>())
                .compose(rxCache.<BookChapterContent>transformer("getBookSectionContent" + bookId + sectionId, BookChapterContent.class, CacheStrategy.firstCache()))
                .map(new Func1<CacheResult<BookChapterContent>, BookChapterContent>() {
                    @Override
                    public BookChapterContent call(CacheResult<BookChapterContent> bookSectionContentCacheResult) {
                        return bookSectionContentCacheResult.getData();
                    }
                });
    }

    public Observable<CacheResult<List<Channel>>> getChannels() {
        return RequestClient
                .getServerAPI()
                .getChannels()
                .map(new HttpResultFunc<List<Channel>>())
                .compose(rxCache.<List<Channel>>transformer(
                        "getChannels",
                        new TypeToken<List<Channel>>() {
                        }.getType(),
                        new BaseStrategy() {
                            @Override
                            public <T> Observable<CacheResult<T>> execute(RxCache rxCache, String key, Observable<T> source, Type type) {
                                Observable<CacheResult<T>> cache = loadCache(rxCache, key, type);
                                Observable<CacheResult<T>> remote = loadRemote(rxCache, key, source, CacheTarget.MemoryAndDisk)
                                        .onErrorReturn(new Func1<Throwable, CacheResult<T>>() {
                                            @Override
                                            public CacheResult<T> call(Throwable throwable) {
                                                throwable.printStackTrace();
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

    public void removeChannelsCache() {
        rxCache.remove("getChannels");
    }

    public Observable<DataList<Book>> getChannelBooks(long channelId, int page, int size) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("page_index", page);
        hashMap.put("page_size", size);
        return RequestClient
                .getServerAPI()
                .getChannelBooks(channelId, hashMap)
                .map(new HttpResultFunc<DataList<Book>>())
                .compose(rxCache.<DataList<Book>>transformer("getChannelBooks"+channelId+page+size,new TypeToken<DataList<Book>>() {
                }.getType(), CacheStrategy.firstRemote()))
                .map(new Func1<CacheResult<DataList<Book>>, DataList<Book>>() {
                    @Override
                    public DataList<Book> call(CacheResult<DataList<Book>> dataListCacheResult) {


                        return dataListCacheResult.getData();
                    }
                });
    }

    public Observable<DataList<Book>> getChannelBookRanking(long channelId, int page, int size) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("page_index", page);
        hashMap.put("page_size", size);
        return RequestClient
                .getServerAPI()
                .getChannelBookRanking(channelId, hashMap)
                .map(new HttpResultFunc<DataList<Book>>());
    }

    public Observable<AppRelease> getLatestReleases() {
        return RequestClient
                .getServerAPI()
                .getLatestReleases()
                .map(new HttpResultFunc<AppRelease>());
    }


    /**
     * 获取书籍详情
     */
    public Observable<BookDetail> getBookDetail(String bookId) {
        return RequestClient
                .getServerAPI()
                .getBookDetail(bookId)
                .map(new HttpResultFunc<BookDetail>());
    }

    /**
     * 获取最新章节
     */
    public Observable<List<LatestChapter>> getLatestChapter(List<String> bookIds) {
        return RequestClient
                .getServerAPI()
                .getLatestChapter(bookIds)
                .map(new HttpResultFunc<List<LatestChapter>>());
    }
}
