package com.youshibi.app.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Chu on 2016/11/20.
 * 职责：管理所有Fragment生命周期，如：统计、恢复
 */

public class BaseSuperFragment extends Fragment {


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //状态恢复
        // Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //状态保存
        // Icepick.saveInstanceState(this, outState);
    }

}
