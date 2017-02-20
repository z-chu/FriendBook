package com.youshibi.app.presentation.classify;

import android.support.annotation.NonNull;

import com.youshibi.app.mvp.MvpFragment;

/**
 * Created by Chu on 2016/12/3.
 */

public class ClassifyFragment extends MvpFragment<ClassifyPresenter> {
    @NonNull
    @Override
    public ClassifyPresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return 0;
    }
}
