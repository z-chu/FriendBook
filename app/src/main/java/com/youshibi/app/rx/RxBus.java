package com.youshibi.app.rx;

import android.content.Context;

import com.google.gson.Gson;
import com.jakewharton.rxrelay.PublishRelay;
import com.jakewharton.rxrelay.Relay;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Subscriber;

/**
 * <p>author : zchu</p>
 * <p>date   : 2017/6/21</p>
 * <p>email  : zchu8073@gmail.com</p>
 *   <ul>
 *       <li>可跨进程传递消息</li>
 *       <li>支持粘性广播</li>
 *       <li>基于RxRelay，自带异常处理，订阅者处理事件出现异常后，订阅者依然能收到事件</li>
 *       <li>不支持背压</li>
 *   </ul>
 *  进程间通讯使用的是BroadcastReceiver.，数据的传输格式为json，不能识别对象的泛型。
 *
 */
public class RxBus {
    private static volatile RxBus mDefaultInstance;
    private final Relay<Object, Object> mBus;
    private final Map<Class<?>, Object> mStickyEventMap;
    private RemoteRxBus mRemoteRxBus;

    public RxBus() {
        this(null);
    }

    public RxBus(RemoteRxBus remoteRxBus) {
        mRemoteRxBus = remoteRxBus;
        mBus =  PublishRelay.create();
        mStickyEventMap = new ConcurrentHashMap<>();
    }

    public static RxBus getDefault() {
        if (mDefaultInstance == null) {
            synchronized (RxBus.class) {
                if (mDefaultInstance == null) {
                    mDefaultInstance = new RxBus();
                }
            }
        }
        return mDefaultInstance;
    }

    /**
     * 连接到其他进程，此方法调用后才可调用postRemote实现跨进程分发事件
     */
    public void connectRemote(Context context) {
        connectRemote(context, new Gson());
    }

    public void connectRemote(Context context, Gson gson) {
        mRemoteRxBus = new RemoteRxBus(context, gson, mBus);
    }

    /**
     * 发送事件
     */
    public void post(Object event) {
        mBus.call(event);
    }

    /**
     * 发送跨进程事件
     */
    public void postRemote(Object event) {
        if (mDefaultInstance == null) {
            throw new NullPointerException("You haven't call connectRemote load a Context");
        }
        mRemoteRxBus.getDefault().post(event);
    }


    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }

    /**
     * 判断当前进程内是否有订阅者
     */
    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    public void reset() {
        mRemoteRxBus.getDefault().release();
        mStickyEventMap.clear();
        mDefaultInstance = null;
    }

    /**
     * Stciky 相关
     */

    /**
     * 发送一个新Sticky事件
     */
    public void postSticky(Object event) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(event.getClass(), event);
        }
        post(event);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    public <T> Observable<T> toObservableSticky(final Class<T> eventType) {
        synchronized (mStickyEventMap) {
            Observable<T> observable = mBus.ofType(eventType);
            final Object event = mStickyEventMap.get(eventType);

            if (event != null) {
                return observable.mergeWith(Observable.create(new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        subscriber.onNext(eventType.cast(event));
                    }
                }));
            } else {
                return observable;
            }
        }
    }

    /**
     * 根据eventType获取Sticky事件
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.get(eventType));
        }
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.remove(eventType));
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickyEvents() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
    }
}