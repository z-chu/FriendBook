package com.youshibi.app.presentation.explore;

import com.youshibi.app.data.bean.Channel;

import java.util.ArrayList;

/**
 * Created by Chu on 2017/8/26.
 */

public class OnSelectionEditFinishEvent {
    public OnSelectionEditFinishEvent(ArrayList<Channel> selectedLabels, ArrayList<Channel> unselectedLabel, ArrayList<Channel> alwaysSelectedLabels) {
        this.selectedLabels = selectedLabels;
        this.unselectedLabel = unselectedLabel;
        this.alwaysSelectedLabels = alwaysSelectedLabels;
    }

    public ArrayList<Channel> selectedLabels, unselectedLabel, alwaysSelectedLabels;
}
