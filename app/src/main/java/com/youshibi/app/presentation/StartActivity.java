package com.youshibi.app.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.youshibi.app.AppRouter;
import com.youshibi.app.base.BaseActivity;
import com.youshibi.app.data.prefs.PreferencesHelper;
import com.youshibi.app.util.FileUtil;


/**
 * Created by zchu on 16-11-22.
 */

public class StartActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 16-11-23 一些UI逻辑、检查缓存版本（更新后缓存是否还可用）
        if (PreferencesHelper.getInstance().isFirstStartApp()) {
            FileUtil.clearFileWithPath(getFilesDir().getPath());
            FileUtil.clearFileWithPath(getCacheDir().getPath());
            PreferencesHelper.getInstance().setFirstStartApp(false);
        }
        if (isLogin()) {
            AppRouter.showMainActivity(this);
            finish();
        }

    }

    private boolean isLogin() {
        return true;
    }
}
