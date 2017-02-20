package com.youshibi.app.data.net.interceptor;


import com.zchu.log.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.zchu.log.Logger.i;

/**
 * 作者: 赵成柱 on 2016/7/14.
 * 网络请求的日志拦过滤器
 */
public class LoggingInterceptor implements Interceptor {
    private static final String SINGLE_DIVIDER = "────────────────────────────────────────────";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        i(String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);
        String bodyString = response.body().string();

        long t2 = System.nanoTime();

        Logger.t(0).i(String.format("Received response for %s in %.1fms%n%s"+SINGLE_DIVIDER+SINGLE_DIVIDER+"\ndata:\n %s", response.request().url(), (t2 - t1) / 1e6d, response.headers(), Logger.fJson(bodyString)));

        return response.newBuilder()
                .body(ResponseBody.create(response.body().contentType(), bodyString))
                .build();
    }
}
