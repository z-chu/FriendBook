package com.youshibi.app.base;

import com.youshibi.app.mvp.MvpBasePresenter;
import com.youshibi.app.mvp.MvpView;

import java.util.HashMap;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 作者: 赵成柱 on 2016/7/19 0019.
 * 方便使用RxJava的Presenter类
 */
public class BaseRxPresenter<V extends MvpView> extends MvpBasePresenter<V> {

    //管理所有的Subscription,便于回收资源
    private CompositeSubscription mSubParent;
    private HashMap<String,Subscription> mSubMap;

    @Override
    public void attachView(V view) {
        super.attachView(view);
        mSubParent=new CompositeSubscription();
        mSubMap=new HashMap<>();
    }

    @Override
    public void detachView() {
        super.detachView();
        //与View断开联系时 ,取消注册RxJava 防止内存溢出
        if (mSubParent.hasSubscriptions()) {
            mSubParent.unsubscribe();
            mSubParent=null;
        }
        if(!mSubMap.isEmpty()){
            mSubMap.clear();
            mSubMap=null;
        }
    }

    /**
     * 通过该方法添加的Subscription，会在Presenter与View解绑时unSubscribe
     */
    protected void addSubscription(Subscription s){
        mSubParent.add(s);
    }

    /**
     * 通过该方法添加相同的key的Subscription，会把原来的Subscription remove and unSubscribe,并会在Presenter与View解绑时unSubscribe
     */
    protected void addSubscription(Subscription s,String key){
        Subscription subscription = mSubMap.get(key);
        if(subscription!=null){
            mSubParent.remove(subscription);
        }
        mSubMap.put(key,s);
        mSubParent.add(s);
    }
}
