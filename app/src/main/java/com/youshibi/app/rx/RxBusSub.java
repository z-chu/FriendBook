package com.youshibi.app.rx;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func0;

/**
 * Created by Chu on 2017/3/11.
 * 用于简化Rxbus订阅事件的编写
 */

public class RxBusSub<T> {

    public static <T> RxBusSub<T> create(Func0<Observable<T>> func0) {
        return new RxBusSub<>(func0);

    }

    public static <T> RxBusSub<T> create(final Class<T> tClass) {
        return RxBusSub.create(new Func0<Observable<T>>() {
            @Override
            public Observable<T> call() {
                return RxBus.getDefault().toObservable(tClass);
            }
        });
    }

    private Func0<Observable<T>> observableFunc0;

    private RxBusSub(Func0<Observable<T>> observableFunc0) {
        this.observableFunc0 = observableFunc0;
    }

    public final RxBusSub<T> observeOn(final Scheduler scheduler) {
        return RxBusSub.create(new Func0<Observable<T>>() {
            @Override
            public Observable<T> call() {
                return observableFunc0.call().observeOn(scheduler);
            }
        });
    }

    public final <R> RxBusSub<R> compose(final Observable.Transformer<? super T, ? extends R> transformer) {
        return RxBusSub.create(new Func0<Observable<R>>() {
            @Override
            public Observable<R> call() {
                return observableFunc0.call().compose(transformer);
            }
        });
    }


    public final void subscribeEvent(final CallBack<T> mCallBack) {
        Subscription subscribe = observableFunc0.call()
                .subscribe(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mCallBack.onError(e);
                        subscribeEvent(mCallBack);
                    }

                    @Override
                    public void onNext(T t) {
                        mCallBack.onNext(t);
                    }
                });
        mCallBack.onSubscribed(subscribe);
    }


    public interface CallBack<T> {
        void onSubscribed(Subscription subscribe);

        void onNext(T t);

        void onError(Throwable e);
    }


}
