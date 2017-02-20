package com.youshibi.app.data.net;

import com.youshibi.app.data.bean.BookRt;
import com.youshibi.app.data.bean.DataListResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Chu on 2016/12/3.
 */

public interface ServerAPI {
    String BASE_URL ="http://192.168.0.10:8099";

    @GET("/book")
    Observable<DataListResult<BookRt>> getBookList(@Query("pageIndex") int pageIndex, @Query("pageSize")int pageSize);
}
