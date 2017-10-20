package com.zchu.labelselection;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * author : zchu
 * date   : 2017/7/10
 * desc   :
 */

class LabelSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // touch 间隔时间  用于分辨是否是 "点击"
    private static final long TOUCH_SPACE_TIME = 100;
    // 动画持续时间
    private static final long ANIM_TIME = 400;
    // touch 点击开始时间
    private long touchStartTime;

    private Context mContext;
    private List<LabelSelectionItem> mData;
    private LayoutInflater mLayoutInflater;
    private RecyclerView mRecyclerView;
    private LabelTitleViewHolder selectedTitleViewHolder;
    private OnItemDragListener onChannelDragListener;
    private OnEditFinishListener onEditFinishListener;
    private OnLabelClickListener onLabelClickListener;
    private boolean isEditing;
    private String selectedName;

    public LabelSelectionAdapter(List<LabelSelectionItem> data) {
        if (data == null) {
            this.mData = new ArrayList<>();
        } else {
            this.mData = data;
        }
    }

    public void setOnLabelClickListener(OnLabelClickListener onLabelClickListener) {
        this.onLabelClickListener = onLabelClickListener;
    }

    public void setOnChannelDragListener(OnItemDragListener onChannelDragListener) {
        this.onChannelDragListener = onChannelDragListener;
    }

    public void setOnEditFinishListener(OnEditFinishListener onEditFinishListener) {
        this.onEditFinishListener = onEditFinishListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (mRecyclerView == null) {
            mRecyclerView = (RecyclerView) parent;
        }

        mContext = parent.getContext();
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(mContext);
        }
        switch (viewType) {
            case LabelSelectionItem.TYPE_LABEL_SELECTED:
                viewHolder = new LabelSelectedViewHolder(mLayoutInflater.inflate(R.layout.item_label_selected, parent, false));
                break;
            case LabelSelectionItem.TYPE_LABEL_ALWAY_SELECTED:
                LabelSelectedViewHolder alwaySelectedViewHolder = new LabelSelectedViewHolder(mLayoutInflater.inflate(R.layout.item_label_selected, parent, false));
                ViewParent parent1 = alwaySelectedViewHolder.ivRemove.getParent();
                if (parent1 != null) {
                    ((ViewGroup) parent1).removeView(alwaySelectedViewHolder.ivRemove);
                }
                viewHolder = alwaySelectedViewHolder;
                break;
            case LabelSelectionItem.TYPE_LABEL_UNSELECTED:
                viewHolder = new LabelUnselectedViewHolder(mLayoutInflater.inflate(R.layout.item_label_unselected, parent, false));
                break;
            case LabelSelectionItem.TYPE_LABEL_SELECTED_TITLE:
                selectedTitleViewHolder = new LabelTitleViewHolder(mLayoutInflater.inflate(R.layout.item_label_title, parent, false));
                selectedTitleViewHolder.tvAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isEditing) {
                            changeEditState(false);
                            selectedTitleViewHolder.tvTitle.setText("切换频道");
                            selectedTitleViewHolder.tvAction.setText("编辑");
                        } else {
                            selectedTitleViewHolder.tvTitle.setText("拖动排序");
                            selectedTitleViewHolder.tvAction.setText("完成");
                            changeEditState(true);
                        }
                    }
                });
                viewHolder = selectedTitleViewHolder;
                break;
            case LabelSelectionItem.TYPE_LABEL_UNSELECTED_TITLE:
                LabelTitleViewHolder unselectedTitleViewHolder = new LabelTitleViewHolder(mLayoutInflater.inflate(R.layout.item_label_title, parent, false));
                unselectedTitleViewHolder.tvAction.setVisibility(View.GONE);
                viewHolder = unselectedTitleViewHolder;
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LabelSelectionItem labelSelectionItem = mData.get(position);
        switch (holder.getItemViewType()) {
            case LabelSelectionItem.TYPE_LABEL_SELECTED:
                bindLabelSelectedViewHolder((LabelSelectedViewHolder) holder, labelSelectionItem);
                break;
            case LabelSelectionItem.TYPE_LABEL_ALWAY_SELECTED:
                bindLabelAlwaySelectedViewHolder((LabelSelectedViewHolder) holder, labelSelectionItem);
                break;
            case LabelSelectionItem.TYPE_LABEL_UNSELECTED:
                bindLabelUnselectedViewHolder((LabelUnselectedViewHolder) holder, labelSelectionItem);
                break;
            case LabelSelectionItem.TYPE_LABEL_SELECTED_TITLE:
                ((LabelTitleViewHolder) holder).tvTitle.setText(labelSelectionItem.getTitle());
                break;
            case LabelSelectionItem.TYPE_LABEL_UNSELECTED_TITLE:
                ((LabelTitleViewHolder) holder).tvTitle.setText(labelSelectionItem.getTitle());
                break;

        }
    }

    private void bindLabelAlwaySelectedViewHolder(LabelSelectedViewHolder holder, final LabelSelectionItem item) {
        if (item.getLabel().getName().equals(selectedName)) {
            holder.tvName.setSelected(true);
        } else {
            holder.tvName.setSelected(false);
        }
        holder.tvName.setText(item.getLabel().getName());
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onLabelClickListener != null) {
                    onLabelClickListener.onLabelClick(view, item);
                }
            }
        });
        if (isEditing) {
            holder.tvName.setBackgroundDrawable(null);
        } else {
            holder.tvName.setBackgroundResource(R.drawable.bg_label_normal);
        }
        holder.tvName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isEditing) {
                    //开启编辑模式
                    changeEditState(true);
                    //  mEditViewHolder.setText(R.id.tvEdit, "完成");
                    selectedTitleViewHolder.tvTitle.setText("拖动排序");
                    selectedTitleViewHolder.tvAction.setText("完成");
                }
                return true;
            }
        });
    }

    private void bindLabelSelectedViewHolder(final LabelSelectedViewHolder holder, final LabelSelectionItem item) {
        holder.tvName.setText(item.getLabel().getName());
        if (isEditing) {
            holder.ivRemove.setVisibility(View.VISIBLE);
        } else {
            holder.ivRemove.setVisibility(View.GONE);
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditing) {
                    unselectedLabel(holder, item);
                } else {
                    if (onLabelClickListener != null) {
                        onLabelClickListener.onLabelClick(v, item);
                    }
                }

            }
        };
        if (item.getLabel().getName().equals(selectedName)) {
            holder.tvName.setSelected(true);
        } else {
            holder.tvName.setSelected(false);
        }
        holder.ivRemove.setOnClickListener(onClickListener);
        holder.tvName.setOnClickListener(onClickListener);
        holder.tvName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isEditing) return false;//正常模式无需监听触摸
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchStartTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (System.currentTimeMillis() - touchStartTime > TOUCH_SPACE_TIME) {
                            //当MOVE事件与DOWN事件的触发的间隔时间大于100ms时，则认为是拖拽starDrag
                            if (onChannelDragListener != null) {
                                onChannelDragListener.onStarDrag(holder);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        touchStartTime = 0;
                        break;
                }
                return false;
            }
        });
        holder.tvName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isEditing) {
                    //开启编辑模式
                    changeEditState(true);
                    //  mEditViewHolder.setText(R.id.tvEdit, "完成");
                    selectedTitleViewHolder.tvTitle.setText("拖动排序");
                    selectedTitleViewHolder.tvAction.setText("完成");
                }
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (onChannelDragListener != null) {
                            onChannelDragListener.onStarDrag(holder);
                        }
                    }
                }, 200);

                return true;
            }
        });

    }

    private void bindLabelUnselectedViewHolder(final LabelUnselectedViewHolder holder, final LabelSelectionItem item) {
        holder.tvName.setText(item.getLabel().getName());
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLabel(holder, item);
            }
        });
    }

    /**
     * 取消选中标签
     * 从选中区域移动到未选中区域
     */
    private void unselectedLabel(LabelSelectedViewHolder viewHolder, LabelSelectionItem item) {
        int otherFirstPosition = getUnselectedFirstPosition();
        int currentPosition = viewHolder.getAdapterPosition();
        //获取到目标View
        View targetView = mRecyclerView.getLayoutManager().findViewByPosition(otherFirstPosition);
        //获取当前需要移动的View
        View currentView = mRecyclerView.getLayoutManager().findViewByPosition(currentPosition);
        // 如果targetView不在屏幕内,则indexOfChild为-1  此时不需要添加动画,因为此时notifyItemMoved自带一个向目标移动的动画
        // 如果在屏幕内,则添加一个位移动画
        if (mRecyclerView.indexOfChild(targetView) >= 0 && otherFirstPosition != -1) {
            RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
            int spanCount = ((GridLayoutManager) manager).getSpanCount();
            int targetX = targetView.getLeft();
            int targetY = targetView.getTop();
            int myChannelSize = getSelectedSize();//这里我是为了偷懒 ，算出来我的频道的大小
            if (myChannelSize % spanCount == 1) {
                //我的频道最后一行 之后一个，移动后
                targetY -= targetView.getHeight();
            }

            //我的频道 移动到 推荐频道的第一个
            item.setItemType(LabelSelectionItem.TYPE_LABEL_UNSELECTED);//改为推荐频道类型

            onMove(currentPosition, otherFirstPosition - 1);
            startAnimation(currentView, targetX, targetY);
        } else {
            item.setItemType(LabelSelectionItem.TYPE_LABEL_UNSELECTED);//改为推荐频道类型
            if (otherFirstPosition == -1) {
                otherFirstPosition = mData.size();
            }
            onMove(currentPosition, otherFirstPosition - 1);
            /*
            if (onChannelDragListener != null)
                onChannelDragListener.onMoveToOtherChannel(currentPosition, otherFirstPosition - 1);*/
        }
    }

    /**
     * 选中标签
     * 从未选中区域移动到选中区域
     */
    private void selectedLabel(LabelUnselectedViewHolder viewHolder, LabelSelectionItem item) {
        int selectedLastPosition = getSelectedLastPosition();
        int currentPosition = viewHolder.getAdapterPosition();
        //获取到目标View
        View targetView = mRecyclerView.getLayoutManager().findViewByPosition(selectedLastPosition);
        //获取当前需要移动的View
        View currentView = mRecyclerView.getLayoutManager().findViewByPosition(currentPosition);
        // 如果targetView不在屏幕内,则indexOfChild为-1  此时不需要添加动画,因为此时notifyItemMoved自带一个向目标移动的动画
        // 如果在屏幕内,则添加一个位移动画
        if (mRecyclerView.indexOfChild(targetView) >= 0 && selectedLastPosition != -1) {
            RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
            int spanCount = ((GridLayoutManager) manager).getSpanCount();
            int targetX = targetView.getLeft() + targetView.getWidth();
            int targetY = targetView.getTop();

            int myChannelSize = getSelectedSize();//这里我是为了偷懒 ，算出来我的频道的大小
            if (myChannelSize % spanCount == 0) {
                //添加到我的频道后会换行，所以找到倒数第4个的位置

                View lastFourthView = mRecyclerView.getLayoutManager().findViewByPosition(getSelectedLastPosition() - (((GridLayoutManager) manager).getSpanCount() - 1));
//                                        View lastFourthView = mRecyclerView.getChildAt(getMyLastPosition() - 3);
                if (lastFourthView != null) {
                    targetX = lastFourthView.getLeft();
                    targetY = lastFourthView.getTop() + lastFourthView.getHeight();
                }
            }


            // 推荐频道 移动到 我的频道的最后一个
            item.setItemType(LabelSelectionItem.TYPE_LABEL_SELECTED);//改为推荐频道类型
            onMove(currentPosition, selectedLastPosition + 1);
            startAnimation(currentView, targetX, targetY);
        } else {
            item.setItemType(LabelSelectionItem.TYPE_LABEL_SELECTED);
            if (selectedLastPosition == -1) {
                selectedLastPosition = 0;
            }
            onMove(currentPosition, selectedLastPosition + 1);

        }
        finishEdit();
    }

    private void onMove(int starPos, int endPos) {
        LabelSelectionItem startItem = mData.get(starPos);
        //先删除之前的位置
        mData.remove(starPos);
        //添加到现在的位置
        mData.add(endPos, startItem);
        this.notifyItemMoved(starPos, endPos);
    }


    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getItemType();
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 添加需要移动的 镜像View
     */
    private ImageView addMirrorView(ViewGroup parent, View view) {
        view.destroyDrawingCache();
        //首先开启Cache图片 ，然后调用view.getDrawingCache()就可以获取Cache图片
        view.setDrawingCacheEnabled(true);
        ImageView mirrorView = new ImageView(view.getContext());
        //获取该view的Cache图片
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        mirrorView.setImageBitmap(bitmap);
        //销毁掉cache图片
        view.setDrawingCacheEnabled(false);
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);//获取当前View的坐标
        int[] parenLocations = new int[2];
        mRecyclerView.getLocationOnScreen(parenLocations);//获取RecyclerView所在坐标
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
        params.setMargins(locations[0], locations[1] - parenLocations[1], 0, 0);
        parent.addView(mirrorView, params);//在RecyclerView的Parent添加我们的镜像View，parent要是FrameLayout这样才可以放到那个坐标点
        return mirrorView;
    }

    private void startAnimation(final View currentView, int targetX, int targetY) {
        final ViewGroup parent = (ViewGroup) mRecyclerView.getParent();
        final ImageView mirrorView = addMirrorView(parent, currentView);
        TranslateAnimation animator = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetX - currentView.getLeft(),
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetY - currentView.getTop());
        // RecyclerView默认移动动画250ms 这里设置360ms 是为了防止在位移动画结束后 remove(view)过早 导致闪烁
        animator.setDuration(ANIM_TIME);
        animator.setFillAfter(true);
        currentView.setVisibility(View.INVISIBLE);//暂时隐藏
        animator.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                parent.removeView(mirrorView);//删除添加的镜像View
                if (currentView.getVisibility() != View.VISIBLE) {
                    currentView.setVisibility(View.VISIBLE);//显示隐藏的View
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mirrorView.startAnimation(animator);
    }

    public int getSelectedSize() {
        int size = 0;
        for (int i = 0; i < mData.size(); i++) {
            LabelSelectionItem labelSelectionItem = mData.get(i);
            if (labelSelectionItem.getItemType() == LabelSelectionItem.TYPE_LABEL_SELECTED || labelSelectionItem.getItemType() == LabelSelectionItem.TYPE_LABEL_ALWAY_SELECTED) {
                size++;
            }
        }
        return size;

    }

    /**
     * 获取推荐频道列表的第一个position
     */
    private int getUnselectedFirstPosition() {
        for (int i = 0; i < mData.size(); i++) {
            LabelSelectionItem labelSelectionItem = mData.get(i);
            if (labelSelectionItem.getItemType() == LabelSelectionItem.TYPE_LABEL_UNSELECTED) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 我的频道最后一个的position
     */
    private int getSelectedLastPosition() {
        for (int i = mData.size() - 1; i >= 0; i--) {
            LabelSelectionItem labelSelectionItem = mData.get(i);
            if (labelSelectionItem.getItemType() == LabelSelectionItem.TYPE_LABEL_SELECTED || labelSelectionItem.getItemType() == LabelSelectionItem.TYPE_LABEL_ALWAY_SELECTED) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 开启编辑模式
     */
    private void changeEditState(boolean state) {
        if (isEditing == state) {
            return;
        }
        if (state) {
            selectedTitleViewHolder.tvTitle.setText("拖动排序");
            selectedTitleViewHolder.tvAction.setText("完成");
        } else {
            selectedTitleViewHolder.tvTitle.setText("切换频道");
            selectedTitleViewHolder.tvAction.setText("编辑");
            finishEdit();

        }
        isEditing = state;
        notifyDataSetChanged();
     /*   int visibleChildCount = mRecyclerView.getChildCount();
        for (int i = 0; i < visibleChildCount; i++) {
            View view = mRecyclerView.getChildAt(i);
            ImageView imgEdit = (ImageView) view.findViewById(R.id.iv_remove);
            if (imgEdit != null) {
                // boolean isVis = imgEdit.getTag() == null ? false : (boolean) imgEdit.getTag();
                imgEdit.setVisibility(state ? View.VISIBLE : View.INVISIBLE);
            }
        }*/
    }

    private void finishEdit() {
        if (onEditFinishListener != null) {
            ArrayList<Label> selectedLabels = new ArrayList<>();
            ArrayList<Label> unselectedLabels = new ArrayList<>();
            ArrayList<Label> alwaySelectedLabels = new ArrayList<>();
            for (LabelSelectionItem labelSelectionItem : mData) {
                if (labelSelectionItem.getItemType() == LabelSelectionItem.TYPE_LABEL_SELECTED) {
                    selectedLabels.add(labelSelectionItem.getLabel());
                } else if (labelSelectionItem.getItemType() == LabelSelectionItem.TYPE_LABEL_UNSELECTED) {
                    unselectedLabels.add(labelSelectionItem.getLabel());
                } else if (labelSelectionItem.getItemType() == LabelSelectionItem.TYPE_LABEL_ALWAY_SELECTED) {
                    alwaySelectedLabels.add(labelSelectionItem.getLabel());
                }
            }
            onEditFinishListener.onEditFinish(selectedLabels, unselectedLabels, alwaySelectedLabels);
        }
    }

    public boolean cancelEdit() {
        if (isEditing) {
            changeEditState(false);
            return true;
        }
        return false;
    }

    public void setNewData(List<LabelSelectionItem> mData) {
        this.mData = mData;
        this.notifyDataSetChanged();
    }

    public List<LabelSelectionItem> getData() {
        return mData;
    }

    private static class LabelSelectedViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private ImageView ivRemove;

        private LabelSelectedViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            ivRemove = (ImageView) itemView.findViewById(R.id.iv_remove);
        }
    }

    private static class LabelUnselectedViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;

        private LabelUnselectedViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    private static class LabelTitleViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvAction;

        private LabelTitleViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAction = (TextView) itemView.findViewById(R.id.tv_action);
        }
    }


    public void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
        notifyDataSetChanged();
    }
}
