package com.youshibi.app.data.net.mock;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by zchu on 16-11-30.
 * 在测试模式下，拦截请求，提供虚假数据交给MockDataProvider去做
 */

public class MockInterceptor implements Interceptor {

    private MockDataProvider mDataProvider;

    public MockInterceptor(MockDataProvider dataProvider){
        mDataProvider=dataProvider;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = null;
        String path = chain.request().url().uri().getPath();
        String responseString = mDataProvider.provideData(path);
        if(responseString!=null){ //DEBUG模式 拦截
            response = new Response.Builder()
                    .code(200)
                    .message(responseString)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                    .addHeader("content-type", "application/json")
                    .build();
        }else{
            response = chain.proceed(chain.request()); //放行通过
        }

        return response;
    }
}
