package com.youshibi.app.data.net;

import com.youshibi.app.data.bean.Book;
import com.youshibi.app.data.bean.BookSectionContent;
import com.youshibi.app.data.bean.BookSectionItem;
import com.youshibi.app.data.bean.BookType;
import com.youshibi.app.data.bean.DataList;
import com.youshibi.app.data.bean.HttpResult;

import java.util.HashMap;
import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Chu on 2016/12/3.
 */

public interface ServerAPI {
    String BASE_URL = "http://115.159.214.196:8099";

    /**
     * 获取所有小说
     */
    @GET("/v1/books")
    Observable<HttpResult<DataList<Book>>> getBookList(@QueryMap HashMap<String, Object> map);

    /**
     * 获取小说类别
     */
    @GET("/v1/book-types")
    Observable<HttpResult<List<BookType>>> getBookType();

    /**
     * 获取小说章节列表
     */
    @GET("/v1/books/{bookId}/chapters")
    Observable<HttpResult<List<BookSectionItem>>> getBookSectionList(@Path("bookId") String bookId,
                                                                     @QueryMap HashMap<String, Object> map);


    /**
     * 获取小说章节中的内容
     */
    @GET("/v1/books/{bookId}/chapters/{position}")
    Observable<HttpResult<BookSectionContent>> getBookSectionContent(@Path("bookId") String bookId,
                                                                     @Path("position") int position,
                                                                     @QueryMap HashMap<String, Object> map);


    /**
     * 获取最新的app版本信息
     */
    @GET("/android/releases/latest")
    Observable<HttpResult> getLatestReleases();


}
