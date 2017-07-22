package com.youshibi.app.data.net.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Chu on 2017/7/22.
 * <p>专用于处理空 JSON 字符串 或者空返回的 {@linkplain Converter.Factory converter} </p>
 * <b>注意必须使用时需要添加到Retrofit的第一个 {@linkplain Converter.Factory converter}</b>
 * <br/><br/>
 * 如果一个 API 请求不需要返回数据，
 * 很可能我们的服务器也就不会返回数据（返回空的 response body），
 * 而空字符串并不是合法的 JSON，所以 Square 实现的 <a href="https://github.com/square/retrofit/tree/master/retrofit-converters/gson">GsonResponseBodyConverter</a> 会不认账，
 * 直接抛出 JSON 解析错误。关于这个问题更多的讨论，可以看一下 Retrofit 的这个 <a href="https://github.com/square/retrofit/issues/1554">issue：#1554 Handle Empty Body<a/>
 */

public class NullOnEmptyConverterFactory extends Converter.Factory {

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(final Type type, final Annotation[] annotations, final Retrofit retrofit) {
        return new NullOnEmptyResponseBodyConverter<>(this, type, annotations, retrofit);
    }
}