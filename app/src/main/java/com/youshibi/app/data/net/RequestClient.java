package com.youshibi.app.data.net;


import com.youshibi.app.BuildConfig;
import com.youshibi.app.data.net.converter.GsonConverterFactory;
import com.youshibi.app.data.net.converter.NullOnEmptyConverterFactory;
import com.youshibi.app.data.net.interceptor.HeaderInterceptor;
import com.youshibi.app.data.net.interceptor.LoggingInterceptor;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * 作者: 赵成柱 on 2016/7/19 0019.
 */
public class RequestClient {


    private static volatile ServerAPI sServerAPI;//单例模式

    public static ServerAPI getServerAPI() {
        if (sServerAPI == null) {
            synchronized (RequestClient.class) {
                if (sServerAPI == null) {
                    OkHttpClient.Builder clientBuilder = getClientBuilder();
                    HashMap<String, String> headerMap = new HashMap<>();
                    headerMap.put("appver", String.valueOf(BuildConfig.VERSION_CODE));
                    clientBuilder.addInterceptor(new HeaderInterceptor(headerMap));
                    //配置日志拦截器
                    if (BuildConfig.DEBUG) {
                        clientBuilder
                                .addInterceptor(new LoggingInterceptor());
                    }

                    sServerAPI = getRetrofitBuilder(ServerAPI.BASE_URL, clientBuilder.build()).build().create(ServerAPI.class);
                }
            }
        }
        return sServerAPI;

    }

    /**
     * @param url    域名
     * @param client okhttp请求客户端
     * @return retrofit的构建器
     */
    private static Retrofit.Builder getRetrofitBuilder(String url, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client);
    }


    private static OkHttpClient.Builder getClientBuilder() {
        return new OkHttpClient.Builder()
                .connectTimeout(HttpConfig.CONNECT_TIME_OUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(HttpConfig.READ_TIME_OUT_SECONDS, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);//重试
        // .writeTimeout(HttpConfig.WRITE_TIME_OUT_SECONDS, TimeUnit.SECONDS);
    }
}
