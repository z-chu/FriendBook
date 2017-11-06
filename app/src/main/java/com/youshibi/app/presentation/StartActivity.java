package com.youshibi.app.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youshibi.app.AppRouter;
import com.youshibi.app.BuildConfig;
import com.youshibi.app.R;
import com.youshibi.app.base.BaseSuperActivity;
import com.youshibi.app.rx.SimpleSubscriber;
import com.youshibi.app.util.DisplayUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;


/**
 * Created by zchu on 16-11-22.
 */

public class StartActivity extends BaseSuperActivity {

    private TextView tvSkip;
    private Subscription subscribe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initDisPlay();
        startCountDown(3);

    }

    private void initDisPlay() {
        tvSkip = findViewById(R.id.tv_skip);
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip();
            }
        });
        View contentView = findViewById(R.id.content_view);
        if (DisplayUtil.hasVirtualNavigationBar(this)) {
            contentView.setPadding(0, 0, 0, DisplayUtil.getNavigationBarHeight(this));
        }
        TextView tvVersionName = findViewById(R.id.tv_version_name);
        tvVersionName.setText(BuildConfig.VERSION_NAME);
        LinearLayout llWelcome = (LinearLayout) findViewById(R.id.ll_welcome);
        TextView tvDate = findViewById(R.id.tv_date);
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy年MM月dd日，EEEE");
        tvDate.setText(format2.format(new Date()));
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
        llWelcome.startAnimation(animation);
    }


    private void startCountDown(int second) {

        final int countTime = second;
        subscribe = Observable.interval(0, 1, TimeUnit.SECONDS)

                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return countTime - increaseTime.intValue();
                    }
                })
                .take(countTime + 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        if (integer == 0) {
                            skip();
                        } else {
                            tvSkip.setText(getString(R.string.format_skip, integer));
                        }
                    }
                });
    }

    private void skip() {
        AppRouter.showMainActivity(StartActivity.this);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(subscribe!=null&&!subscribe.isUnsubscribed()){
            subscribe.unsubscribe();
        }
    }
}
