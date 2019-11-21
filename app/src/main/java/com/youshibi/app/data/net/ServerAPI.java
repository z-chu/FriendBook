package com.youshibi.app.data.net;

import com.youshibi.app.data.bean.AppRelease;
import com.youshibi.app.data.bean.Book;
import com.youshibi.app.data.bean.BookChapter;
import com.youshibi.app.data.bean.BookChapterContent;
import com.youshibi.app.data.bean.BookDetail;
import com.youshibi.app.data.bean.BookType;
import com.youshibi.app.data.bean.Channel;
import com.youshibi.app.data.bean.DataList;
import com.youshibi.app.data.bean.HttpResult;
import com.youshibi.app.data.bean.LatestChapter;

import java.util.HashMap;
import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Chu on 2016/12/3.
 */

public interface ServerAPI {
    String BASE_URL = "http://47.104.108.36:8089";

    /**
     * 获取所有小说
     */
    @GET("/v1/books")
    Observable<HttpResult<DataList<Book>>> getBookList(@QueryMap HashMap<String, Object> map);

    /**
     * 获取小说详情
     */
    @GET("/v1/books/{bookId}")
    Observable<HttpResult<BookDetail>> getBookDetail(@Path("bookId") String bookId);

    /**
     * 书籍最新章节信息
     */
    @POST("v1/books/latest-chapter")
    Observable<HttpResult<List<LatestChapter>>> getLatestChapter(@Body List<String> bookIds);

    /**
     * 搜索小说
     */
    @GET("/v1/search/books")
    Observable<HttpResult<DataList<Book>>> searchBooks(@QueryMap HashMap<String, Object> map);

    /**
     * 获取小说类别
     */
    @GET("/v1/book-types")
    Observable<HttpResult<List<BookType>>> getBookType();

    /**
     * 获取小说章节列表
     */
    @GET("/v1/books/{bookId}/chapters")
    Observable<HttpResult<List<BookChapter>>> getBookSectionList(@Path("bookId") String bookId,
                                                                     @QueryMap HashMap<String, Object> map);


    /**
     * 获取小说章节中的内容
     */
    @GET("/v1/books/{bookId}/chapters/{position}")
    Observable<HttpResult<BookChapterContent>> getBookSectionContent(@Path("bookId") String bookId,
                                                                     @Path("position") int position,
                                                                     @QueryMap HashMap<String, Object> map);

    /**
     * 获取所有频道
     */
    @GET("/v1/channels")
    Observable<HttpResult<List<Channel>>> getChannels();

    /**
     * 获取频道内容-榜单
     */
    @GET("/v1/channels/books/{channelId}")
    Observable<HttpResult<DataList<Book>>> getChannelBooks(@Path("channelId") long channelId,
                                                           @QueryMap HashMap<String, Object> map);

    /**
     * 获取频道内容-书籍列表
     */
    @GET("/v1/channels/book-ranking/{channelId}")
    Observable<HttpResult<DataList<Book>>> getChannelBookRanking(@Path("channelId") long channelId,
                                                                 @QueryMap HashMap<String, Object> map);

    /**
     * 获取最新的app版本信息
     */
    @GET("/android/releases/latest")
    Observable<HttpResult<AppRelease>> getLatestReleases();


}
