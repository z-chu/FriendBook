package com.youshibi.app.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


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
    //    MobclickAgent.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
       // MobclickAgent.onPause(this);
    }


}
