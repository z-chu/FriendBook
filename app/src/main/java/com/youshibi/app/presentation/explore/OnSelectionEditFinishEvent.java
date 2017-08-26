package com.youshibi.app.presentation.explore;

import com.youshibi.app.data.bean.BookType;

import java.util.ArrayList;

/**
 * Created by Chu on 2017/8/26.
 */

public class OnSelectionEditFinishEvent {
    public OnSelectionEditFinishEvent(ArrayList<BookType> selectedLabels, ArrayList<BookType> unselectedLabel, ArrayList<BookType> alwaysSelectedLabels) {
        this.selectedLabels = selectedLabels;
        this.unselectedLabel = unselectedLabel;
        this.alwaysSelectedLabels = alwaysSelectedLabels;
    }

    public ArrayList<BookType> selectedLabels, unselectedLabel, alwaysSelectedLabels;
}
