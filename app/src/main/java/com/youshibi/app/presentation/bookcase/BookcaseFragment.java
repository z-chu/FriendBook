package com.youshibi.app.presentation.bookcase;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;
import com.umeng.analytics.MobclickAgent;
import com.youshibi.app.AppRouter;
import com.youshibi.app.R;
import com.youshibi.app.base.BaseListFragment;
import com.youshibi.app.data.db.table.BookTb;
import com.youshibi.app.util.DensityUtil;
import com.zchu.labelselection.OnItemDragListener;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Chu on 2016/12/3.
 */

public class BookcaseFragment extends BaseListFragment<BookcasePresenter> implements View.OnClickListener, BookcaseContract.View, FragmentBackHandler, OnItemDragListener, BookcaseAdapter.OnItemSelectedListener {


    private View bottomEditBar;
    private View bottomItemBookShare;
    private View bottomItemSelectAll;
    private View bottomItemDelete;
    private View bottomItemBookDetails;
    private AppCompatImageView ivBottomItemSelectAll;
    private TextView tvBottomItemSelectAll;
    private TextView tvSelectedCount;
    private AppCompatImageView ivBottomItemBookShare;
    private TextView tvBottomItemBookShare;
    private AppCompatImageView ivBottomItemDelete;
    private TextView tvBottomItemDelete;
    private AppCompatImageView ivBottomItemBookDetails;
    private TextView tvBottomItemBookDetails;

    private Toolbar toolbarBookcaseEdit;
    private Spinner spinnerSort;
    private BookcaseSortSpinnerAdapter spinnerAdapter;
    private BookcaseAdapter bookcaseAdapter;


    private OnBookCaseEditListener mEditListener;
    private ItemTouchHelper itemTouchHelper;


    public static BookcaseFragment newInstance() {
        return new BookcaseFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bookcase;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBookCaseEditListener) {
            mEditListener = (OnBookCaseEditListener) context;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.inflateMenu(R.menu.bookcase);
        toolbar.getMenu().findItem(R.id.action_search).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AppRouter.showSearchActivity(getContext());
                return true;
            }
        });
        toolbarBookcaseEdit = view.findViewById(R.id.toolbar_bookcase_edit);
        spinnerSort = view.findViewById(R.id.spinner_sort);
        String[] bookcaseSpinnerArray = getResources().getStringArray(R.array.bookcase_spinner);
        spinnerAdapter = new BookcaseSortSpinnerAdapter(getContext(), bookcaseSpinnerArray);
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                TextView tv = (TextView) view;
                Drawable drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.ic_triangle);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv.setCompoundDrawables(null, null, drawable, null);
                tv.setTextColor(Color.WHITE);    //设置颜色
                tv.setPadding(0, 0, 0, 0);
                tv.setCompoundDrawablePadding(DensityUtil.dp2px(view.getContext(), 8));//设置图片和text之间的间距
                tv.setTextSize(16.0f);    //设置大小
                TextPaint paint = tv.getPaint();
                paint.setFakeBoldText(true);
                tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);   //设置居中
                spinnerAdapter.setSelectedPosition(pos);
                getPresenter().dispatchSortSpinnerItemSelected(pos);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        toolbarBookcaseEdit.inflateMenu(R.menu.bookcase_edit);
        toolbarBookcaseEdit.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                toggleEditMenu();
                bookcaseAdapter.cancelEdit();
                return true;
            }
        });
        spinnerSort.setAdapter(spinnerAdapter);
        spinnerSort.setSelection(getPresenter().getDefaultSelectedSortSpinnerItem(),true);
    }

    @Override
    public void setRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        int i = getResources().getDisplayMetrics().widthPixels;
        int y = DensityUtil.dp2px(getContext(), 90);
        int p = DensityUtil.dp2px(getContext(), 15);
        int padding = (3 * y + 2 * 3 * p + 2 * p - i) / (2 * 3);

        recyclerView.setPadding(
                padding,
                DensityUtil.dp2px(getContext(), 8),
                padding,
                DensityUtil.dp2px(getContext(), 8)
        );
    }

    /**
     * 切换菜单栏的可视状态
     * 默认是隐藏的
     */
    private void toggleEditMenu() {
        if (toolbarBookcaseEdit.getVisibility() == VISIBLE) {
            toolbarBookcaseEdit.setVisibility(GONE);
            contentView.setEnabled(true);
            if (mEditListener != null) {
                getBottomEditBar(mEditListener.getBottomGroup()).setVisibility(View.GONE);
            }
            getPresenter().finishEdit();
        } else {
            toolbarBookcaseEdit.setVisibility(VISIBLE);
            contentView.setRefreshing(false);
            contentView.setEnabled(false);
            if (mEditListener != null) {
                getBottomEditBar(mEditListener.getBottomGroup()).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void setAdapter(BaseQuickAdapter adapter) {
        adapter.setEmptyView(R.layout.view_empty_bookcase, recyclerView);
        adapter.bindToRecyclerView(recyclerView);

        this.bookcaseAdapter = (BookcaseAdapter) adapter;
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback((BaseItemDraggableAdapter) adapter);
        itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        bookcaseAdapter.enableDragItem(itemTouchHelper);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            MobclickAgent.onPageEnd(getClass().getPackage().getName() + ".BookcaseFragment");
        } else {
            MobclickAgent.onPageStart(getClass().getPackage().getName() + ".BookcaseFragment");
        }
    }

    @NonNull
    @Override
    public BookcasePresenter createPresenter() {
        return new BookcasePresenter();
    }


    public View getBottomEditBar(ViewGroup viewGroup) {
        if (bottomEditBar == null) {
            bottomEditBar = LayoutInflater
                    .from(viewGroup.getContext())
                    .inflate(R.layout.layout_bookcase_bottom_edit_bar, viewGroup, false);
            bottomItemBookShare = bottomEditBar.findViewById(R.id.bottom_item_book_share);
            bottomItemSelectAll = bottomEditBar.findViewById(R.id.bottom_item_select_all);
            ivBottomItemSelectAll = bottomEditBar.findViewById(R.id.iv_bottom_item_select_all);
            tvBottomItemSelectAll = bottomEditBar.findViewById(R.id.tv_bottom_item_select_all);
            bottomItemDelete = bottomEditBar.findViewById(R.id.bottom_item_delete);
            bottomItemBookDetails = bottomEditBar.findViewById(R.id.bottom_item_book_details);
            tvSelectedCount = bottomEditBar.findViewById(R.id.tv_selected_count);
            ivBottomItemBookShare = bottomEditBar. findViewById(R.id.iv_bottom_item_book_share);
            tvBottomItemBookShare = bottomEditBar. findViewById(R.id.tv_bottom_item_book_share);
            ivBottomItemDelete =  bottomEditBar.findViewById(R.id.iv_bottom_item_delete);
            tvBottomItemDelete = bottomEditBar.findViewById(R.id.tv_bottom_item_delete);
            ivBottomItemBookDetails = bottomEditBar.findViewById(R.id.iv_bottom_item_book_details);
            tvBottomItemBookDetails = bottomEditBar.findViewById(R.id.tv_bottom_item_book_details);
            bindOnClickLister(this, bottomItemBookShare, bottomItemSelectAll, bottomItemDelete, bottomItemBookDetails);
            viewGroup.addView(bottomEditBar);
            bookcaseAdapter.setOnItemSelectedListener(this);
        }
        return bottomEditBar;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottom_item_book_share:

                break;
            case R.id.bottom_item_select_all:
                if (ivBottomItemSelectAll.isSelected()) {
                    bookcaseAdapter.clearSelectedAllItem();
                } else {
                    bookcaseAdapter.selectedAllItem();
                }
                break;
            case R.id.bottom_item_delete:
                if (bookcaseAdapter.getSelectedBookTbs().size() > 0) {
                    showDeleteConfirmDialog();
                }
                break;
            case R.id.bottom_item_book_details:

                break;
        }
    }

    private void showDeleteConfirmDialog() {
        new MaterialDialog
                .Builder(getActivity())
                .title("确认删除")
                .content("真的要将这" + bookcaseAdapter.getSelectedBookTbs().size() + "本书从书架中删除吗？")
                .positiveText("删除")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        getPresenter().deleteItems(bookcaseAdapter.getSelectedBookTbs());
                        toggleEditMenu();
                        bookcaseAdapter.cancelEdit();
                    }
                })
                .negativeText("取消")
                .show();
    }

    @Override
    public void showEditMode() {
        toggleEditMenu();
    }

    @Override
    public void startDrag(int position) {
        itemTouchHelper.startDrag(recyclerView.findViewHolderForLayoutPosition(position));
    }

    @Override
    public void onItemMove(int starPos, int endPos) {

    }

    @Override
    public void onStarDrag(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public void onSelectedItemsChange(ArrayList<BookTb> items) {
        if (items.size() > 0) {
            tvSelectedCount.setVisibility(View.VISIBLE);
            tvSelectedCount.setText(String.valueOf(items.size()));
            bottomItemBookShare.setEnabled(true);
            ivBottomItemBookShare.setEnabled(true);
            tvBottomItemBookShare.setEnabled(true);
            ivBottomItemBookDetails.setEnabled(true);
            tvBottomItemBookDetails.setEnabled(true);
            bottomItemBookDetails.setEnabled(true);
            bottomItemDelete.setEnabled(true);
            ivBottomItemDelete.setEnabled(true);
            tvBottomItemDelete.setEnabled(true);

        } else {
            tvSelectedCount.setVisibility(View.GONE);
            bottomItemBookShare.setEnabled(false);
            ivBottomItemBookShare.setEnabled(false);
            tvBottomItemBookShare.setEnabled(false);
            ivBottomItemBookDetails.setEnabled(false);
            tvBottomItemBookDetails.setEnabled(false);
            bottomItemBookDetails.setEnabled(false);
            bottomItemDelete.setEnabled(false);
            ivBottomItemDelete.setEnabled(false);
            tvBottomItemDelete.setEnabled(false);
        }
        if (items.size() == bookcaseAdapter.getData().size()) {
            ivBottomItemSelectAll.setSelected(true);
            tvBottomItemSelectAll.setSelected(true);
            tvBottomItemSelectAll.setText(getString(R.string.deselect_all));
        } else {
            ivBottomItemSelectAll.setSelected(false);
            tvBottomItemSelectAll.setSelected(false);
            tvBottomItemSelectAll.setText(getString(R.string.select_all));
        }

    }


    public interface OnBookCaseEditListener {
        ViewGroup getBottomGroup();
    }

    @Override
    public boolean onBackPressed() {
        if (toolbarBookcaseEdit.getVisibility() == VISIBLE) {
            toggleEditMenu();
            bookcaseAdapter.cancelEdit();
            //外理返回键
            return true;
        }
        return false;
    }







}
