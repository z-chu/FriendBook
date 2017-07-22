package com.youshibi.app.rx;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.MainThread;

import com.youshibi.app.exception.NetNotConnectedException;
import com.zchu.log.Logger;

import rx.Subscriber;

/**
 * Created by zchu on 17-2-23.
 */

public abstract class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private ProgressDialogHandler mProgressDialogHandler;

    private Context mContext;
    /**
     * 订阅时网络是否必须可以，不可用则抛出NetNotConnectedException
     */
    protected final boolean isMustNetAvailable;

    @MainThread
    public ProgressSubscriber(Context context) {
        this(false, context, true);
    }


    @MainThread
    public ProgressSubscriber(Context context, boolean cancelable) {
        this(false, context, cancelable);
    }

    @MainThread
    public ProgressSubscriber(boolean isMustNetAvailable, Context context) {
        this(isMustNetAvailable, context, true);
    }

    @MainThread
    public ProgressSubscriber(boolean isMustNetAvailable, Context context, boolean cancelable) {
        this.mContext = context;
        this.isMustNetAvailable = isMustNetAvailable;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, cancelable);
    }

    protected void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    protected void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onStart() {
        if (isMustNetAvailable && isNetConnected(mContext)) {
            throw new NetNotConnectedException();
        }
        showProgressDialog();
    }

    private static boolean isNetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    /**
     * onError之后不会走onCompleted，所有注意也要关闭掉ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        Logger.e(e);
        /*if (e instanceof HttpException) {
            ErrorUtils.showError(mContext, (HttpException) e);
        } else {
            ErrorUtils.showError(mContext, new HttpException(-1, e.getMessage()));
        }*/
        dismissProgressDialog();
    }


    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
            mProgressDialogHandler = null;
        }
    }
}
