package com.youshibi.app.rx;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

/**
 * Created by zchu on 17-2-23.
 */

public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private Dialog mDialog;

    private Context mContext;
    private boolean mCancelable;
    private ProgressCancelListener mProgressCancelListener;

    public ProgressDialogHandler(Context context, ProgressCancelListener mProgressCancelListener,
                                 boolean cancelable) {
        super();
        this.mContext = context;
        this.mProgressCancelListener = mProgressCancelListener;
        this.mCancelable = cancelable;
    }

    private void initProgressDialog(){
        if (mDialog == null) {
           // mDialog = new LoadingDialog(mContext); // FIXME: 2017/7/7 添加全局加载中对话框
            mDialog.setCancelable(mCancelable);

            if (mCancelable) {
                mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mProgressCancelListener.onCancelProgress();
                    }
                });
                mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mContext=null;
                        mDialog = null;
                    }
                });
            }

            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }
    }

    private void dismissProgressDialog(){
        if (mDialog != null&&mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }
}