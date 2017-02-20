package com.youshibi.app.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 作者: 赵成柱 on 2016/7/13.
 */
public class HttpResult<T> implements Serializable {

    //将访问权限设为public的原因参考http://www.zhihu.com/question/21401198
    @SerializedName("Code")
    public int code;

    @SerializedName("Msg")
    public String message;

    @SerializedName("Data")
    public T data;


    
    public boolean isSuccessful(){
        return code==0;
    }
    
}
