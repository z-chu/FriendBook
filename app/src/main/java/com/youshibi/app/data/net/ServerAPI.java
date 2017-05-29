package com.youshibi.app.data.net;

import com.youshibi.app.data.bean.Book;
import com.youshibi.app.data.bean.BookSectionItem;
import com.youshibi.app.data.bean.BookType;
import com.youshibi.app.data.bean.DataList;
import com.youshibi.app.data.bean.HttpResult;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Chu on 2016/12/3.
 */

public interface ServerAPI {
    String BASE_URL = "http://api.laiyoushu.com";

    /**
     * 获取所有小说
     */
    @GET("/bookList")
    Observable<HttpResult<DataList<Book>>> getBookList(@QueryMap HashMap<String, Object> map);

    /**
     * 获取小说类别
     */
    @GET("/book/GetBookType")
    Observable<HttpResult<ArrayList<BookType>>> getBookType();

    /**
     * 获取小说章节列表
     */
    @GET("/Book/GetChapterList")
    Observable<HttpResult<ArrayList<BookSectionItem>>> getBookSectionList(@QueryMap HashMap<String, Object> map);


    /**
     * 获取小说章节中的内容
     */
    @GET("/Book/GetChapterList")
    Observable<HttpResult> getBookSectionContent(@QueryMap HashMap<String, Object> map);


}
