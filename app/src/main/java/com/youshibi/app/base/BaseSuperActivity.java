package com.youshibi.app.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;


/**
 * 作者: 赵成柱 on 2016/7/13.
 * 职责：管理所有Activity生命周期，如：统计、恢复
 */
public class BaseSuperActivity extends AppCompatActivity {

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
      /*  PushAgent.getInstance(this).onAppStart();
        //状态恢复
        Icepick.restoreInstanceState(this, savedInstanceState);*/
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //状态保存
        //  Icepick.saveInstanceState(this, outState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isCountingPage()) {
            countingPageStart();
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isCountingPage()) {
            countingPageEnd();
        }
        MobclickAgent.onPause(this);
    }

    protected void countingPageStart() {
        MobclickAgent.onPageStart(this.getClass().getName());
    }

    protected void countingPageEnd() {
        MobclickAgent.onPageEnd(this.getClass().getName());
    }


    /**
     * 子类通过重新此方法,选择是否统计Activity的跳转路径,
     * 当Activity由Framgent组成,应该统计Framgent的跳转路径而不是统计Activity
     */
    protected boolean isCountingPage() {
        return true;
    }


}
