package com.youshibi.app.data.net;


import com.youshibi.app.data.net.interceptor.LoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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
                    //配置日志拦截器
                    clientBuilder.interceptors().add(new LoggingInterceptor());

                    sServerAPI = getRetrofitBuilder(sServerAPI.BASE_URL, clientBuilder.build()).build().create(ServerAPI.class);
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
                //接口基地址
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                //Json转换器
                .addConverterFactory(GsonConverterFactory.create())
                //Rxjava转换器：将结果转成已Observable的形式返回
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
