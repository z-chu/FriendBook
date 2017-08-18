package com.youshibi.app.data.net.converter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Chu on 2017/7/22.
 */

 class NullOnEmptyResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private Converter.Factory factory;
    private Type type;
    private Annotation[] annotations;
    private Retrofit retrofit;

    NullOnEmptyResponseBodyConverter(@Nullable Converter.Factory factory, Type type, Annotation[] annotations, Retrofit retrofit) {
        this.factory = factory;
        this.type = type;
        this.annotations = annotations;
        this.retrofit = retrofit;
    }

    @Override
    public T convert(@NonNull ResponseBody value) throws IOException {
        if (value.contentLength() == 0) {
            return null;
        }
        return retrofit
                .<T>nextResponseBodyConverter(factory, type, annotations)
                .convert(value);
    }
}
