package com.youshibi.app.presentation.explore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.youshibi.app.R;
import com.youshibi.app.base.BaseActivity;
import com.youshibi.app.data.bean.BookType;
import com.youshibi.app.ui.help.ToolbarHelper;
import com.zchu.labelselection.Label;
import com.zchu.labelselection.LabelSelectionFragment;

import java.util.ArrayList;

/**
 * author : zchu
 * date   : 2017/8/25
 * desc   :
 */

public class BookTypeSelectionActivity extends BaseActivity {

    private static final String EXTRA_SELECTED_LABELS = "selected_labels";
    private static final String EXTRA_ALWAY_SELECTED_LABELS = "alway_selected_labels";
    private static final String EXTRA_UNSELECTED_LABELS = "unselected_labels";

    public static Intent newIntent(Context context, ArrayList<BookType> selectedLabels, ArrayList<BookType> unselectedLabels, ArrayList<BookType> alwaySelectedLabels) {
        Intent intent = new Intent(context, BookTypeSelectionActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_SELECTED_LABELS, selectedLabels);
        intent.putParcelableArrayListExtra(EXTRA_UNSELECTED_LABELS, unselectedLabels);
        intent.putParcelableArrayListExtra(EXTRA_ALWAY_SELECTED_LABELS, alwaySelectedLabels);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_type_selection);
        ToolbarHelper.initToolbar(this, R.id.toolbar, true, "全部频道");
        //创建置顶且默认选择标签
        ArrayList<BookType> alwaySelectedBookTypes = getIntent().getParcelableArrayListExtra(EXTRA_ALWAY_SELECTED_LABELS);
        ArrayList<Label> alwaySelectedLabels = null;
        if (alwaySelectedBookTypes != null && alwaySelectedBookTypes.size() > 0) {
            alwaySelectedLabels = new ArrayList<>();
            for (BookType alwaySelectedBookType : alwaySelectedBookTypes) {
                alwaySelectedLabels.add(new Label(alwaySelectedBookType.getId(), alwaySelectedBookType.getTypeName()));
            }
        }


        //创建默认选择标签
        ArrayList<BookType> selectedBookTypes = getIntent().getParcelableArrayListExtra(EXTRA_SELECTED_LABELS);
        ArrayList<Label> selectedLabels = null;
        if (selectedBookTypes != null && selectedBookTypes.size() > 0) {
            selectedLabels = new ArrayList<>();
            for (BookType selectedBookType : selectedBookTypes) {
                selectedLabels.add(new Label(selectedBookType.getId(), selectedBookType.getTypeName()));
            }
        }

        //其他标签
        ArrayList<BookType> unselectedBookTypes = getIntent().getParcelableArrayListExtra(EXTRA_UNSELECTED_LABELS);
        ArrayList<Label> unselectedLabels = null;
        if (selectedBookTypes != null && selectedBookTypes.size() > 0) {
            unselectedLabels = new ArrayList<>();
            for (BookType unselectedBookType : unselectedBookTypes) {
                unselectedLabels.add(new Label(unselectedBookType.getId(), unselectedBookType.getTypeName()));
            }
        }
        //创建LabelSelectionFragment绑定到你的Activity即可
        LabelSelectionFragment labelSelectionFragment = LabelSelectionFragment.newInstance(selectedLabels, unselectedLabels, alwaySelectedLabels);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_view, labelSelectionFragment)
                .commit();
    }


    @Override
    protected void onSlideStateChanged(int state) {

    }

    @Override
    public void onSlideCancel() {

    }
}
