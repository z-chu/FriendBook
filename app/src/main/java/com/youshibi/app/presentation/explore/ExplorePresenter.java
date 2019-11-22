package com.youshibi.app.presentation.explore;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.youshibi.app.base.BaseRxPresenter;
import com.youshibi.app.data.DataManager;
import com.youshibi.app.data.bean.BookType;
import com.youshibi.app.data.bean.Channel;
import com.youshibi.app.data.prefs.PreferencesHelper;
import com.youshibi.app.presentation.book.BookFragment;
import com.youshibi.app.presentation.book.ChannelType;
import com.youshibi.app.rx.RxBus;
import com.youshibi.app.rx.SimpleSubscriber;
import com.zchu.rxcache.data.CacheResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chu on 2017/8/26.
 */

public class ExplorePresenter extends BaseRxPresenter<ExploreContract.View> implements ExploreContract.Presenter {

    private List<Channel> mChannels;
    private ArrayList<Channel> mAlwaysSelectedChannels;
    private ArrayList<Channel> mCommonSelectedChannels;
    private ArrayList<Channel> mUnselectedChannels;
    private boolean isReBind = false;

    private HashMap<Channel, Fragment> fragmentHashMap = new HashMap<>();


    @Override
    public void start() {
        super.start();
        if (mChannels != null) {
            isReBind = true;
        }
    }

    @Override
    public void loadData() {
        if (isReBind) {
            processData(mChannels, PreferencesHelper.getInstance().getSelectedChannels());
            return;
        }
        getView().showLoading();
        Subscription subscribe = DataManager
                .getInstance()
                .getChannels()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<CacheResult<List<Channel>>>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (isViewAttached()) {
                            getView().showError(handleException(e));
                        }
                        if (e instanceof NullPointerException) {
                            DataManager.getInstance().removeChannelsCache();
                            PreferencesHelper.getInstance().setSelectedChannels(null);
                            mChannels = null;
                            mAlwaysSelectedChannels = null;
                            mCommonSelectedChannels = null;
                            mUnselectedChannels = null;
                        }
                    }

                    @Override
                    public void onNext(CacheResult<List<Channel>> cacheResult) {
                        List<Channel> data = cacheResult.getData();
                        if (data != null && data.size() > 0) {


//                            Channel fake = new Channel();
//                            fake.setChannelId(124l);
//                            fake.setChannelName("app本地测试数据");
//                            fake.setChannelType(ChannelType.BOOKS);
//                            fake.setSelectedStatus(1);
//                            fake.setContentQueryId("local");
//                            fake.setContentType("abc");
//
//
//                            Channel fake2 = new Channel();
//                            fake2.setChannelId(1245l);
//                            fake2.setChannelName("app本地测试数据2");
//                            fake2.setChannelType(ChannelType.BOOKS);
//                            fake2.setSelectedStatus(1);
//                            fake2.setContentQueryId("local2");
//                            fake2.setContentType("abc");
//
//
//                            data.add(fake);
//                            data.add(fake2);

                            processData(data, PreferencesHelper.getInstance().getSelectedChannels());
                        }
                    }
                });
        addSubscription2Destroy(subscribe);
    }


    private void processData(@NonNull List<Channel> channels, @Nullable List<Channel> selectedBookLabels) {
        if (mChannels == null || isReBind) {
            mChannels = channels;
            boolean isFirst = (selectedBookLabels == null || selectedBookLabels.size() == 0);
            if (isFirst && selectedBookLabels == null) {
                selectedBookLabels = new ArrayList<>();
            }
            if (!isFirst) {
                for (int i = selectedBookLabels.size() - 1; i >= 0; i--) {
                    if (selectedBookLabels.get(i).getSelectedStatus() == BookType.STATUS_ALWAYS_SELECTED) {
                        selectedBookLabels.remove(i);
                    }
                }
                mCommonSelectedChannels = new ArrayList<>(selectedBookLabels);
            } else {
                mCommonSelectedChannels = new ArrayList<>();
            }
            mAlwaysSelectedChannels = new ArrayList<>();
            mUnselectedChannels = new ArrayList<>();
            for (Channel channel : channels) {
             /*   if (isFirst) {
                    if (!bookType.isUnselected()) {
                        selectedBookLabels.add(bookType);
                    }
                }*/
                if (channel.getSelectedStatus() == BookType.STATUS_ALWAYS_SELECTED) {
                    mAlwaysSelectedChannels.add(channel);
                } else {

                    if (isFirst && channel.getSelectedStatus() == BookType.STATUS_DEFAULT_SELECTED) {
                        mCommonSelectedChannels.add(channel);
                        selectedBookLabels.add(channel);
                        continue;
                    }
                    if (!selectedBookLabels.contains(channel)) {
                        mUnselectedChannels.add(channel);

                    }
                }
            }
            // if (!isFirst) {
            selectedBookLabels.addAll(0, mAlwaysSelectedChannels);
            // }
            Fragment[] fragments = null;
            String[] pageTitles = null;
            if (selectedBookLabels.size() > 0) {
                fragments = new Fragment[selectedBookLabels.size()];
                pageTitles = new String[selectedBookLabels.size()];
                for (int i = 0; i < selectedBookLabels.size(); i++) {
                    Channel channel = selectedBookLabels.get(i);
                    Fragment fragment = fragmentHashMap.get(channel);
                    if (fragment == null) {
                        fragments[i] = BookFragment.newInstance(channel.getChannelType(), channel.getChannelId());
                        fragmentHashMap.put(channel, fragments[i]);
                    } else {
                        fragments[i] = fragment;
                    }
                    pageTitles[i] = channel.getChannelName();
                }
            }
            if (isViewAttached()) {
                if (fragments != null) {
                    getView().setTabContent(fragments, pageTitles);
                }
                getView().showContent();
                subscribeEvent();
            }
        } else {
            mChannels = channels;
        }


    }

    private void subscribeEvent() {
        Subscription subscribe = RxBus
                .getDefault()
                .toObservable(OnSelectionEditFinishEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<OnSelectionEditFinishEvent>() {
                    @Override
                    public void onNext(OnSelectionEditFinishEvent onSelectionEditFinishEvent) {

                        mCommonSelectedChannels = onSelectionEditFinishEvent.selectedLabels;
                        mUnselectedChannels = onSelectionEditFinishEvent.unselectedLabel;
                        ArrayList<Channel> selectedChannels = new ArrayList<>(mCommonSelectedChannels);
                        selectedChannels.addAll(0, mAlwaysSelectedChannels);
                        PreferencesHelper.getInstance().setSelectedChannels(selectedChannels);
                        Fragment[] fragments;
                        String[] pageTitles;
                        if (selectedChannels.size() > 0) {
                            fragments = new Fragment[selectedChannels.size()];
                            pageTitles = new String[selectedChannels.size()];
                            for (int i = 0; i < selectedChannels.size(); i++) {
                                Channel channel = selectedChannels.get(i);
                                Fragment fragment = fragmentHashMap.get(channel);
                                if (fragment == null) {
                                    fragments[i] = BookFragment.newInstance(channel.getChannelType(), channel.getChannelId());
                                    fragmentHashMap.put(channel, fragments[i]);
                                } else {
                                    fragments[i] = fragment;
                                }
                                pageTitles[i] = channel.getChannelName();
                            }
                        } else {
                            fragments = new Fragment[0];
                            pageTitles = new String[0];
                        }
                        if (isViewAttached()) {
                            getView().setTabContent(fragments, pageTitles);
                        }
                    }
                });
        addSubscription2Destroy(subscribe);
        addSubscription2Destroy(
                RxBus
                        .getDefault()
                        .toObservable(OnChannelClickEvent.class)
                        .subscribe(new SimpleSubscriber<OnChannelClickEvent>() {
                            @Override
                            public void onNext(OnChannelClickEvent onChannelClickEvent) {
                                if (isViewAttached()) {
                                    getView().setSelectedTab(onChannelClickEvent.channel.getChannelName());
                                }
                            }
                        })
        );

    }

    @Override
    public void openBookTypeSelection(Context context) {
        Intent intent = BookTypeSelectionActivity
                .newIntent(context, mCommonSelectedChannels, mUnselectedChannels,
                        mAlwaysSelectedChannels, getView().getSelectedTab());
        context.startActivity(intent);
    }
}
