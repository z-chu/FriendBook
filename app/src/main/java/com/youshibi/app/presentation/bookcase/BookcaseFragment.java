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
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;
import com.umeng.analytics.MobclickAgent;
import com.youshibi.app.AppRouter;
import com.youshibi.app.R;
import com.youshibi.app.base.BaseListFragment;
import com.youshibi.app.util.DensityUtil;

import retrofit2.http.HEAD;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Chu on 2016/12/3.
 */

public class BookcaseFragment extends BaseListFragment<BookcasePresenter> implements View.OnClickListener, BookcaseContract.View, FragmentBackHandler {


    private View bottomEditBar;
    private FrameLayout bottomItemBookShare;
    private RelativeLayout bottomItemSelectAll;
    private AppCompatImageView ivBottomItemSelectAll;
    private FrameLayout bottomItemDelete;
    private FrameLayout bottomItemBookDetails;

    private Toolbar toolbarBookcaseEdit;
    private Spinner spinnerSort;
    private BookcaseSortSpinnerAdapter spinnerAdapter;
    private BookcaseAdapter bookcaseAdapter;


    private OnBookCaseEditListener mEditListener;


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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        toolbarBookcaseEdit.inflateMenu(R.menu.bookcase_edit);
        spinnerSort.setAdapter(spinnerAdapter);
    }

    @Override
    public void setRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

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
        } else {
            toolbarBookcaseEdit.setVisibility(VISIBLE);
            contentView.setEnabled(false);
            if (mEditListener != null) {
                getBottomEditBar(mEditListener.getBottomGroup()).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void setAdapter(BaseQuickAdapter adapter) {
        adapter.setEmptyView(R.layout.view_empty_bookcase, recyclerView);
        recyclerView.setAdapter(adapter);
        this.bookcaseAdapter = (BookcaseAdapter) adapter;
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
            bottomItemBookShare = (FrameLayout) bottomEditBar.findViewById(R.id.bottom_item_book_share);
            bottomItemSelectAll = (RelativeLayout) bottomEditBar.findViewById(R.id.bottom_item_select_all);
            ivBottomItemSelectAll = (AppCompatImageView) bottomEditBar.findViewById(R.id.iv_bottom_item_select_all);
            bottomItemDelete = (FrameLayout) bottomEditBar.findViewById(R.id.bottom_item_delete);
            bottomItemBookDetails = (FrameLayout) bottomEditBar.findViewById(R.id.bottom_item_book_details);
            bindOnClickLister(this, bottomItemBookShare, bottomItemSelectAll, bottomItemDelete, bottomItemBookDetails);
            viewGroup.addView(bottomEditBar);
        }
        return bottomEditBar;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottom_item_book_share:

                break;
            case R.id.bottom_item_select_all:

                break;
            case R.id.bottom_item_delete:

                break;
            case R.id.bottom_item_book_details:

                break;
        }
    }

    @Override
    public void showEditMode() {
        toggleEditMenu();
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
