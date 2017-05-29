package com.youshibi.app.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Chu on 2016/12/3.
 */

public class DataList<T> implements Serializable {
    public int Count;
    public ArrayList<T> DataList;
}
