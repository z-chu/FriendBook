package com.youshibi.app.presentation.explore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.youshibi.app.R;
import com.youshibi.app.base.BaseActivity;
import com.youshibi.app.data.bean.Channel;
import com.youshibi.app.rx.RxBus;
import com.youshibi.app.ui.help.ToolbarHelper;
import com.zchu.labelselection.Label;
import com.zchu.labelselection.LabelSelectionFragment;
import com.zchu.labelselection.LabelSelectionItem;
import com.zchu.labelselection.OnEditFinishListener;
import com.zchu.labelselection.OnLabelClickListener;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * author : zchu
 * date   : 2017/8/25
 * desc   :
 */

public class BookTypeSelectionActivity extends BaseActivity implements OnEditFinishListener {

    private static final String EXTRA_SELECTED_LABELS = "selected_labels";
    private static final String EXTRA_ALWAY_SELECTED_LABELS = "alway_selected_labels";
    private static final String EXTRA_UNSELECTED_LABELS = "unselected_labels";
    private static final String EXTRA_SELECTED_NAME = "selected_name";

    private LabelSelectionFragment labelSelectionFragment;
    private ArrayList<Label> selectedLabels, unselectedLabels;
    private ArrayList<Channel> alwaysSelectedBookTypes;
    private ArrayList<Channel> oldSelectedBookTypes;

    public static Intent newIntent(Context context, ArrayList<Channel> selectedLabels,
                                   ArrayList<Channel> unselectedLabels,
                                   ArrayList<Channel> alwaySelectedLabels,
                                   String selectedName) {
        Intent intent = new Intent(context, BookTypeSelectionActivity.class);
        intent.putExtra(EXTRA_SELECTED_LABELS, selectedLabels);
        intent.putExtra(EXTRA_UNSELECTED_LABELS, unselectedLabels);
        intent.putExtra(EXTRA_ALWAY_SELECTED_LABELS, alwaySelectedLabels);
        intent.putExtra(EXTRA_SELECTED_NAME, selectedName);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_type_selection);
        ToolbarHelper.initToolbar(this, R.id.toolbar, true, "全部频道");
        //创建置顶且默认选择标签
        alwaysSelectedBookTypes = (ArrayList<Channel>) getIntent().getSerializableExtra(EXTRA_ALWAY_SELECTED_LABELS);
        ArrayList<Label> alwaySelectedLabels = null;
        if (alwaysSelectedBookTypes != null && alwaysSelectedBookTypes.size() > 0) {
            alwaySelectedLabels = new ArrayList<>();
            for (Channel alwaySelectedBookType : alwaysSelectedBookTypes) {
                alwaySelectedLabels.add(new Label<>(alwaySelectedBookType.getChannelName(), alwaySelectedBookType));
            }
        }


        //创建默认选择标签
        ArrayList<Channel> selectedBookTypes = (ArrayList<Channel>) getIntent().getSerializableExtra(EXTRA_SELECTED_LABELS);
        oldSelectedBookTypes = selectedBookTypes;
        ArrayList<Label> selectedLabels = null;
        if (selectedBookTypes != null && selectedBookTypes.size() > 0) {
            selectedLabels = new ArrayList<>();
            for (Channel selectedBookType : selectedBookTypes) {
                selectedLabels.add(new Label<>(selectedBookType.getChannelName(), selectedBookType));
            }
        }

        //其他标签
        ArrayList<Channel> unselectedBookTypes = (ArrayList<Channel>) getIntent().getSerializableExtra(EXTRA_UNSELECTED_LABELS);
        ArrayList<Label> unselectedLabels = null;
        if (unselectedBookTypes != null && unselectedBookTypes.size() > 0) {
            unselectedLabels = new ArrayList<>();
            for (Channel unselectedBookType : unselectedBookTypes) {
                unselectedLabels.add(new Label<>(unselectedBookType.getChannelName(), unselectedBookType));
            }
        }
        //创建LabelSelectionFragment绑定到你的Activity即可
        labelSelectionFragment = LabelSelectionFragment.newInstance(selectedLabels,
                unselectedLabels,
                alwaySelectedLabels,
                getIntent().getStringExtra(EXTRA_SELECTED_NAME)
        );
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_view, labelSelectionFragment)
                .commit();
        labelSelectionFragment.setOnLabelClickListener(new OnLabelClickListener() {
            @Override
            public void onLabelClick(View view, final LabelSelectionItem item) {
                notifyDataSetChanged();
                Serializable data = item.getLabel().getData();
                if (oldSelectedBookTypes.contains(data) || alwaysSelectedBookTypes.contains(data)) {
                    RxBus.getDefault().post(new OnChannelClickEvent((Channel) item.getLabel().getData()));
                } else {
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RxBus.getDefault().post(new OnChannelClickEvent((Channel) item.getLabel().getData()));
                        }
                    }, 500);
                }
                BookTypeSelectionActivity.super.finish();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (labelSelectionFragment.cancelEdit()) {
                return true;//不执行父类点击事件
            }
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    @Override
    protected void onSlideStateChanged(int state) {

    }

    @Override
    public void onSlideCancel() {

    }

    @Override
    public void onEditFinish(ArrayList<Label> selectedLabels, ArrayList<Label> unselectedLabel, ArrayList<Label> alwaySelectedLabels) {
        this.selectedLabels = selectedLabels;
        this.unselectedLabels = unselectedLabel;
    }

    @Override
    public void finish() {
        notifyDataSetChanged();
        super.finish();
    }

    private void notifyDataSetChanged() {
        ArrayList<Channel> selectedBookTypes = null;
        if (selectedLabels != null) {
            selectedBookTypes = new ArrayList<>();
            for (Label selectedLabel : selectedLabels) {
                selectedBookTypes.add((Channel) selectedLabel.getData());
            }
        }
        ArrayList<Channel> unselectedBookTypes = null;
        if (unselectedLabels != null) {
            unselectedBookTypes = new ArrayList<>();
            for (Label selectedLabel : unselectedLabels) {
                unselectedBookTypes.add((Channel) selectedLabel.getData());
            }
        }
        if (selectedBookTypes != null || unselectedBookTypes != null) {
            RxBus.getDefault().post(new OnSelectionEditFinishEvent(selectedBookTypes, unselectedBookTypes, alwaysSelectedBookTypes));
        }
    }
}
