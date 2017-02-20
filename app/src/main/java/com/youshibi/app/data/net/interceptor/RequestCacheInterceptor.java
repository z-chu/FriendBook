package com.youshibi.app.data.net.interceptor;


import com.youshibi.app.AppContext;
import com.youshibi.app.util.SystemTool;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;



/**
 * @Description 缓存拦截器
 * @Author MoseLin
 * @Date 2016/7/9.
 */
public class RequestCacheInterceptor implements Interceptor
{

    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();
        if (!SystemTool.checkNet(AppContext.context())) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response originalResponse = chain.proceed(request);
        if (SystemTool.checkNet(AppContext.context())) {
            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            String cacheControl = request.cacheControl().toString();
            return originalResponse.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        } else {
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                    .removeHeader("Pragma")
                    .build();
        }
    }
}
