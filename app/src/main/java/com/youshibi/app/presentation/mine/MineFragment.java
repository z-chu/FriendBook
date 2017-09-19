package com.youshibi.app.presentation.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.youshibi.app.AppRouter;
import com.youshibi.app.R;
import com.youshibi.app.base.BaseFragment;

/**
 * Created by Chu on 2017/5/28.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {

    public static MineFragment newInstance() {


        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindOnClickLister(view, this,
                R.id.mine_app_setting,
                R.id.mine_app_night_mode,
                R.id.mine_app_share,
                R.id.mine_app_good_reputation,
                R.id.mine_app_feedback,
                R.id.mine_app_about);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            MobclickAgent.onPageEnd(getClass().getPackage().getName() + ".MineFragment");
        } else {
            MobclickAgent.onPageStart(getClass().getPackage().getName() + ".MineFragment");
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_app_setting:

                break;
            case R.id.mine_app_night_mode:

                break;
            case R.id.mine_app_share:

                break;
            case R.id.mine_app_good_reputation:
                AppRouter.showAppMarket(getContext());
                break;
            case R.id.mine_app_feedback:

                break;
            case R.id.mine_app_about:

                break;

        }
    }
}
