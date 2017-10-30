package com.youshibi.app.presentation.main;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.youshibi.app.BuildConfig;
import com.youshibi.app.R;
import com.youshibi.app.base.BaseRxPresenter;
import com.youshibi.app.data.DataManager;
import com.youshibi.app.data.bean.AppRelease;
import com.youshibi.app.presentation.bookcase.BookcaseFragment;
import com.youshibi.app.presentation.explore.ExploreFragment;
import com.youshibi.app.presentation.mine.MineFragment;
import com.youshibi.app.rx.SchedulersCompat;
import com.youshibi.app.rx.SimpleSubscriber;

import java.util.List;

import me.shenfan.updateapp.UpdateService;
import rx.Subscription;

/**
 * Created by Chu on 2016/12/3.
 */

public class MainPresenter extends BaseRxPresenter<MainContract.View> implements MainContract.Presenter {

    private FragmentManager mFragmentManager;

    private int contentContainerId;

    @Override
    public void start() {
        super.start();
        checkAppUpdate();
    }

    private void checkAppUpdate() {
        Subscription subscribe = DataManager
                .getInstance()
                .getLatestReleases()
                .compose(SchedulersCompat.<AppRelease>applyIoSchedulers())
                .subscribe(new SimpleSubscriber<AppRelease>() {
                    @Override
                    public void onNext(AppRelease release) {
                        if (isViewAttached() && release.getVersionCode() > BuildConfig.VERSION_CODE) {
                            showAppUpdateDialog((Activity) getView().provideContext(), release);
                        }
                    }
                });
        addSubscription2Destroy(subscribe);
    }

    private void showAppUpdateDialog(final Activity activity, final AppRelease release) {
        new MaterialDialog
                .Builder(activity)
                .title("新版本："+release.getVersionName())
                .content(release.getReleaseNotes())
                .positiveText("立即下载")
                .negativeText("以后再说")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        UpdateService.Builder.create(release.getSourceFileUrl()).build(activity);
                    }
                })
                .show();

    }

    @Override
    public void initContentContainer(@NonNull final FragmentManager fragmentManager, @IdRes int contentContainerId) {
        this.mFragmentManager = fragmentManager;
        this.contentContainerId = contentContainerId;
    }


    @Override
    public boolean dispatchTabSelectedTabId(@IdRes int tabId) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        Fragment selectedFragment = mFragmentManager.findFragmentByTag(String.valueOf(tabId));
        switch (tabId) {
            case R.id.tab_bookcase:
                if (selectedFragment == null) {
                    selectedFragment = BookcaseFragment.newInstance();
                    fragmentTransaction.add(contentContainerId, selectedFragment, String.valueOf(tabId));
                }
                checkedTab(fragmentTransaction, tabId);
                getView().switchBookcase(R.id.tab_bookcase);
                return true;
            case R.id.tab_explore:
                if (selectedFragment == null) {
                    selectedFragment = ExploreFragment.newInstance();
                    fragmentTransaction.add(contentContainerId, selectedFragment, String.valueOf(tabId));
                }
                checkedTab(fragmentTransaction, tabId);
                getView().switchExplore(R.id.tab_bookcase);
                return true;
            case R.id.tab_mine:
                if (selectedFragment == null) {
                    selectedFragment = MineFragment.newInstance();
                    fragmentTransaction.add(contentContainerId, selectedFragment, String.valueOf(tabId));
                }
                checkedTab(fragmentTransaction, tabId);
                getView().switchMine(R.id.tab_bookcase);
                return true;
        }

        return false;
    }

    private void checkedTab(FragmentTransaction fragmentTransaction, @IdRes int tabId) {
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment.getTag().equals(String.valueOf(tabId))) {
                    fragmentTransaction.show(fragment);
                } else {
                    fragmentTransaction.hide(fragment);
                }
            }
        }
        fragmentTransaction.commit();
    }

}
