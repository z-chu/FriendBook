package com.youshibi.app.presentation.mine;

import com.youshibi.app.R;
import com.youshibi.app.base.BaseFragment;

/**
 * Created by Chu on 2017/5/28.
 */

public class MineFragment extends BaseFragment {

    public static MineFragment newInstance() {


        MineFragment fragment = new MineFragment();
        return fragment;
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }
}
