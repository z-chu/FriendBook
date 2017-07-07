package com.youshibi.app.rx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Process;
import android.util.Base64;

import com.google.gson.Gson;
import com.jakewharton.rxrelay.PublishRelay;
import com.jakewharton.rxrelay.Relay;

import rx.Observable;

/**
 * Created by zchu on 17-3-10.
 * 一个跨进程的RxBus，数据的传输格式为json，进程间通讯使用的是BroadcastReceiver
 */

class RemoteRxBus {

    private static volatile RemoteRxBus mDefaultInstance;
    private final Relay<Object, Object> mBus;
    private final BroadcastReceiver mReceiver;
    private Context mContext;
    private String mIntentAction;
    private Gson mGson;
    private int mPid;

    RemoteRxBus(Context context, Gson gson, Relay<Object, Object> bus) {
        this.mContext = context;
        this.mGson = gson;
        this.mPid = Process.myPid();
        this.mBus = bus;
        this.mIntentAction = Base64.encodeToString(getClass().getName().getBytes(), Base64.DEFAULT);
        this.mReceiver = new EventReceiver(mBus, gson, context.getPackageName(), mPid);
        context.registerReceiver(mReceiver, new IntentFilter(mIntentAction));
    }


    public static void connect(Context context) {
        connect(context, new Gson());
    }

    public static void connect(Context context, Gson gson) {
        connect(context.getApplicationContext(), gson, PublishRelay.create());
    }

    public static void connect(Context context, Gson gson, Relay<Object, Object> bus) {
        if (mDefaultInstance == null) {
            if (context == null) {
                throw new IllegalArgumentException("You cannot start a load on a null Context");
            }
            mDefaultInstance = new RemoteRxBus(context.getApplicationContext(), gson, bus);
        }
    }


    public static RemoteRxBus getDefault() {
        if (mDefaultInstance == null) {
            throw new NullPointerException("You haven't call connect load a Context");
        }
        return mDefaultInstance;
    }

    /**
     * 发送事件
     */
    public void post(Object event) {
        mBus.call(event);
        send(event);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }


    public void release() {
        mContext.unregisterReceiver(mReceiver);
        mDefaultInstance = null;
    }

    private void send(Object event) {
        Intent intent = new Intent(mIntentAction);
        intent.setPackage(mContext.getPackageName());
        intent.putExtra(EventReceiver.EXTRA_CLASS_TYPE, event.getClass());
        intent.putExtra(EventReceiver.EXTRA_CONTENT_JSON, mGson.toJson(event));
        intent.putExtra(EventReceiver.EXTRA_PROCESS_PID, mPid);
        mContext.sendBroadcast(intent);
    }

    private static class EventReceiver extends BroadcastReceiver {
        private static final String EXTRA_CLASS_TYPE = "class_type";
        private static final String EXTRA_CONTENT_JSON = "content_json";
        private static final String EXTRA_PROCESS_PID = "process_pid";

        private final Gson gson;
        private final String packageName;
        private final Relay<Object, Object> bus;
        private final int pid;

        public EventReceiver(Relay<Object, Object> bus, Gson gson, String packageName, int pid) {
            this.bus = bus;
            this.gson = gson;
            this.packageName = packageName;
            this.pid = pid;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (context.getPackageName().equals(packageName) && pid != intent.getIntExtra(EXTRA_PROCESS_PID, 0)) {
                String json = intent.getStringExtra(EXTRA_CONTENT_JSON);
                Class aClass = (Class) intent.getSerializableExtra(EXTRA_CLASS_TYPE);
                Object event = gson.fromJson(json, aClass);
                bus.call(event);
            }

        }


    }
}
