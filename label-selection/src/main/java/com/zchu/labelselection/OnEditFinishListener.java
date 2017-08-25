package com.zchu.labelselection;

import java.util.ArrayList;

/**
 * Created by Chu on 2017/7/11.
 */

public interface OnEditFinishListener {
    void onEditFinish(ArrayList<Label> selectedLabels, ArrayList<Label> unselectedLabel, ArrayList<Label> alwaySelectedLabels);
}
