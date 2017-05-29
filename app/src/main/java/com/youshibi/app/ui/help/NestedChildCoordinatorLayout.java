package com.youshibi.app.ui.help;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Chu on 2017/5/28.
 */

public class NestedChildCoordinatorLayout extends CoordinatorLayout implements NestedScrollingChild {

    private NestedScrollingChildHelper nestedScrollingChildHelper = new NestedScrollingChildHelper(this);

    public NestedChildCoordinatorLayout(Context context) {
        this(context,null);
    }

    public NestedChildCoordinatorLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NestedChildCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setNestedScrollingEnabled(true);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return nestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return nestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable @Size(value = 2) int[] consumed, @Nullable @Size(value = 2) int[] offsetInWindow) {
        return nestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable @Size(value = 2) int[] offsetInWindow) {
        return nestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return nestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return nestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        dispatchNestedFling(velocityX, velocityY, consumed);
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        dispatchNestedPreFling(velocityX, velocityY);
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);
        dispatchNestedPreScroll(dx, dy, new int[2], null);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        dispatchNestedScroll(0, 0, dxUnconsumed, dyUnconsumed, null);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        super.onNestedScrollAccepted(child, target, nestedScrollAxes);
        startNestedScroll(nestedScrollAxes);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        nestedScrollingChildHelper.startNestedScroll(nestedScrollAxes);
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        nestedScrollingChildHelper.stopNestedScroll();
        super.onStopNestedScroll(target);
    }

    public void setNestedScrollingEnabled(boolean paramBoolean)
    {
        nestedScrollingChildHelper.setNestedScrollingEnabled(paramBoolean);
    }

    public boolean startNestedScroll(int paramInt)
    {
        return nestedScrollingChildHelper.startNestedScroll(paramInt);
    }

    public void stopNestedScroll()
    {
        nestedScrollingChildHelper.stopNestedScroll();
    }
}
