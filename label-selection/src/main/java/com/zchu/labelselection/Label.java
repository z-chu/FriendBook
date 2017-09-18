package com.zchu.labelselection;

import java.io.Serializable;

/**
 * author : zchu
 * date   : 2017/7/10
 * desc   :
 */

public class Label<T extends Serializable> implements Serializable {

    public Label(String name) {
        this.name = name;
    }

    public Label(String name, T data) {
        this.name = name;
        this.data = data;
    }

    private String name;
    private T data;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
