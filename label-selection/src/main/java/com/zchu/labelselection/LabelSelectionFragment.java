package com.zchu.labelselection;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * author : zchu
 * date   : 2017/7/10
 * desc   :
 */

public class LabelSelectionFragment extends Fragment implements OnItemDragListener {
    private static final String BUNDLE_SELECTED_LABELS = "selected_labels";
    private static final String BUNDLE_ALWAY_SELECTED_LABELS = "alway_selected_labels";
    private static final String BUNDLE_UNSELECTED_LABELS = "unselected_labels";
    private static final String BUNDLE_SELECTED_NAME = "selected_name";


    private RecyclerView mRecyclerView;
    private LabelSelectionAdapter mLabelSelectionAdapter;
    private ItemTouchHelper mHelper;
    private OnEditFinishListener mOnEditFinishListener;
    private OnLabelClickListener onLabelClickListener;

    public static LabelSelectionFragment newInstance(ArrayList<Label> selectedLabels, ArrayList<Label> unselectedLabels) {

        return newInstance(selectedLabels, unselectedLabels, null);
    }

    public static LabelSelectionFragment newInstance(ArrayList<Label> selectedLabels, ArrayList<Label> unselectedLabels, ArrayList<Label> alwaySelectedLabels) {

        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_SELECTED_LABELS, selectedLabels);
        args.putSerializable(BUNDLE_ALWAY_SELECTED_LABELS, alwaySelectedLabels);
        args.putSerializable(BUNDLE_UNSELECTED_LABELS, unselectedLabels);
        LabelSelectionFragment fragment = new LabelSelectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static LabelSelectionFragment newInstance(ArrayList<Label> selectedLabels,
                                                     ArrayList<Label> unselectedLabels,
                                                     ArrayList<Label> alwaySelectedLabels,
                                                     String selectedName) {

        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_SELECTED_LABELS, selectedLabels);
        args.putSerializable(BUNDLE_ALWAY_SELECTED_LABELS, alwaySelectedLabels);
        args.putSerializable(BUNDLE_UNSELECTED_LABELS, unselectedLabels);
        args.putString(BUNDLE_SELECTED_NAME, selectedName);
        LabelSelectionFragment fragment = new LabelSelectionFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = new RecyclerView(inflater.getContext());
    /*    mRecyclerView.setPadding(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()),
                0
        );*/
        mRecyclerView.setClipToPadding(false);
        mRecyclerView.setClipChildren(false);
        return mRecyclerView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEditFinishListener) {
            mOnEditFinishListener = (OnEditFinishListener) context;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            final ArrayList<LabelSelectionItem> labelSelectionItems = new ArrayList<>();
            labelSelectionItems.add(new LabelSelectionItem(LabelSelectionItem.TYPE_LABEL_SELECTED_TITLE, "切换栏目"));
            ArrayList<Label> alwaySelectedLabels = (ArrayList<Label>) arguments.getSerializable(BUNDLE_ALWAY_SELECTED_LABELS);
            if (alwaySelectedLabels != null && alwaySelectedLabels.size() > 0) {
                for (Label alwaySelectedLabel : alwaySelectedLabels) {
                    labelSelectionItems.add(new LabelSelectionItem(LabelSelectionItem.TYPE_LABEL_ALWAY_SELECTED, alwaySelectedLabel));
                }
            }
            ArrayList<Label> selectedLabels = (ArrayList<Label>) arguments.getSerializable(BUNDLE_SELECTED_LABELS);
            if (selectedLabels != null && selectedLabels.size() > 0) {
                for (Label selectedLabel : selectedLabels) {
                    labelSelectionItems.add(new LabelSelectionItem(LabelSelectionItem.TYPE_LABEL_SELECTED, selectedLabel));
                }
            }
            labelSelectionItems.add(new LabelSelectionItem(LabelSelectionItem.TYPE_LABEL_UNSELECTED_TITLE, "点击添加更多标签"));
            ArrayList<Label> unselectedLabels = (ArrayList<Label>) arguments.getSerializable(BUNDLE_UNSELECTED_LABELS);
            if (unselectedLabels != null && unselectedLabels.size() > 0) {

                for (Label unselectedLabel : unselectedLabels) {
                    labelSelectionItems.add(new LabelSelectionItem(LabelSelectionItem.TYPE_LABEL_UNSELECTED, unselectedLabel));
                }
            }
            mLabelSelectionAdapter = new LabelSelectionAdapter(labelSelectionItems);
            mLabelSelectionAdapter.setSelectedName(getArguments().getString(BUNDLE_SELECTED_NAME));
            mLabelSelectionAdapter.setOnLabelClickListener(onLabelClickListener);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemViewType = mLabelSelectionAdapter.getItemViewType(position);
                    return itemViewType == LabelSelectionItem.TYPE_LABEL_SELECTED || itemViewType == LabelSelectionItem.TYPE_LABEL_UNSELECTED || itemViewType == LabelSelectionItem.TYPE_LABEL_ALWAY_SELECTED ? 1 : 4;
                }
            });
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.setAdapter(mLabelSelectionAdapter);

            ItemDragHelperCallBack callBack = new ItemDragHelperCallBack(this);
            mLabelSelectionAdapter.setOnChannelDragListener(this);
            mLabelSelectionAdapter.setOnEditFinishListener(mOnEditFinishListener);
            mHelper = new ItemTouchHelper(callBack);
            mHelper.attachToRecyclerView(mRecyclerView);

        }


    }

    @Override
    public void onItemMove(int starPos, int endPos) {
        List<LabelSelectionItem> data = mLabelSelectionAdapter.getData();
        LabelSelectionItem labelSelectionItem = data.get(starPos);
        //先删除之前的位置
        data.remove(starPos);
        //添加到现在的位置
        data.add(endPos, labelSelectionItem);
        mLabelSelectionAdapter.notifyItemMoved(starPos, endPos);
    }

    @Override
    public void onStarDrag(RecyclerView.ViewHolder viewHolder) {
        mHelper.startDrag(viewHolder);
    }

    public boolean cancelEdit() {
        return mLabelSelectionAdapter.cancelEdit();
    }

    public void setOnLabelClickListener(OnLabelClickListener onLabelClickListener) {
        this.onLabelClickListener = onLabelClickListener;
        if (mLabelSelectionAdapter != null) {
            mLabelSelectionAdapter.setOnLabelClickListener(onLabelClickListener);
        }
    }


}
