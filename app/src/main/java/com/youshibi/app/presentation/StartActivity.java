package com.youshibi.app.presentation;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.youshibi.app.AppRouter;
import com.youshibi.app.R;
import com.youshibi.app.base.BaseSuperActivity;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by zchu on 16-11-22.
 */

public class StartActivity extends BaseSuperActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        TextView tvDate = findViewById(R.id.tv_date);
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy年MM月dd日，EEEE");
        tvDate.setText(format2.format(new Date()));
        // TODO: 16-11-23 一些UI逻辑、检查缓存版本（更新后缓存是否还可用）
        /*if (PreferencesHelper.getInstance().isFirstStartApp()) {
            FileUtil.clearFileWithPath(getFilesDir().getPath());
            FileUtil.clearFileWithPath(getCacheDir().getPath());
            PreferencesHelper.getInstance().setFirstStartApp(false);
        }*/
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isLogin()) {
                            AppRouter.showMainActivity(StartActivity.this);
                            finish();
                        }
                    }
                }, 2000);
      /*  if (isLogin()) {
            AppRouter.showMainActivity(this);
            finish();
        }*/

    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, 0);
    }

    private boolean isLogin() {
        return true;
    }
}
