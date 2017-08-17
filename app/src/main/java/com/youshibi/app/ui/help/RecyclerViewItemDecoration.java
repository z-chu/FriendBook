package com.youshibi.app.ui.help;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.regex.Pattern;

/**
 * RecycleView item decoration
 * Created by Eminem Lu on 24/11/15.
 * Email arjinmc@hotmail.com
 */
public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * mode for direction
     * draw the itemdrecoration orientation
     */
    public static final int MODE_HORIZONTAL = 0;
    public static final int MODE_VERTICAL = 1;
    public static final int MODE_GRID = 2;

    /**
     * default decoration color
     */
    private static final String DEFAULT_COLOR = "#bdbdbd";

    /**
     * image resource id for R.java
     */
    private int mDrawableRid = 0;
    /**
     * decoration color
     */
    private int mColor = Color.parseColor(DEFAULT_COLOR);
    /**
     * decoration thickness
     */
    private int mThickness;
    /**
     * decoration dash with
     */
    private int mDashWidth = 0;
    /**
     * decoration dash gap
     */
    private int mDashGap = 0;
    private boolean mFirstLineVisible;
    private boolean mLastLineVisible;
    private int mPaddingStart = 0;
    private int mPaddingEnd = 0;
    /**
     * border line for grid mode
     */
    private boolean mGridLeftVisible;
    private boolean mGridRightVisible;
    private boolean mGridTopVisible;
    private boolean mGridBottomVisible;
    /**
     * spacing for grid mode
     */
    public int mGridHorizontalSpacing;
    public int mGridVerticalSpacing;
    /**
     * direction mode for decoration
     */
    private int mMode;
    private RecyclerView mParent;

    private Paint mPaint;

    private Bitmap mBmp;
    private NinePatch mNinePatch;
    /**
     * choose the real thickness for image or thickness
     */
    private int mCurrentThickness;
    /**
     * sign for if the resource image is a ninepatch image
     */
    private Boolean hasNinePatch = false;

    public RecyclerViewItemDecoration() {
    }

    public void setParams(Context context, Param params) {

        this.mMode = params.mode;
        this.mDrawableRid = params.drawableRid;
        this.mColor = params.color;
        this.mThickness = params.thickness;
        this.mDashGap = params.dashGap;
        this.mDashWidth = params.dashWidth;
        this.mPaddingStart = params.paddingStart;
        this.mPaddingEnd = params.paddingEnd;
        this.mFirstLineVisible = params.firstLineVisible;
        this.mLastLineVisible = params.lastLineVisible;
        this.mGridLeftVisible = params.gridLeftVisible;
        this.mGridRightVisible = params.gridRightVisible;
        this.mGridTopVisible = params.gridTopVisible;
        this.mGridBottomVisible = params.gridBottomVisible;
        this.mGridHorizontalSpacing = params.gridHorizontalSpacing;
        this.mGridVerticalSpacing = params.gridVerticalSpacing;

        this.mParent = params.parent;

        if (mParent != null) compatibleWithLayoutManager(mParent);

        this.mBmp = BitmapFactory.decodeResource(context.getResources(), mDrawableRid);
        if (mBmp != null) {

            if (mBmp.getNinePatchChunk() != null) {
                hasNinePatch = true;
                mNinePatch = new NinePatch(mBmp, mBmp.getNinePatchChunk(), null);
            }

            if (mMode == MODE_HORIZONTAL)
                mCurrentThickness = mThickness == 0 ? mBmp.getHeight() : mThickness;
            if (mMode == MODE_VERTICAL)
                mCurrentThickness = mThickness == 0 ? mBmp.getWidth() : mThickness;
        }

        initPaint();

    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mThickness);
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        if (parent.getChildCount() == 0) return;
        mPaint.setColor(mColor);
        if (mMode == MODE_HORIZONTAL) {
            drawHorinzontal(c, parent);
        } else if (mMode == MODE_VERTICAL) {
            drawVertical(c, parent);
        } else if (mMode == MODE_GRID) {
            drawGrid(c, parent);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int viewPosition = parent.getChildLayoutPosition(view);

        if (mMode == MODE_HORIZONTAL) {

            if (!(!mLastLineVisible &&
                    viewPosition == parent.getAdapter().getItemCount() - 1)) {
                if (mDrawableRid != 0) {
                    outRect.set(0, 0, 0, mCurrentThickness);
                } else {
                    outRect.set(0, 0, 0, mThickness);
                }
            }

            if (mFirstLineVisible && viewPosition == 0) {
                if (mDrawableRid != 0) {
                    outRect.set(0, mCurrentThickness, 0, mCurrentThickness);
                } else {
                    outRect.set(0, mThickness, 0, mThickness);
                }
            }

        } else if (mMode == MODE_VERTICAL) {
            if (!(!mLastLineVisible &&
                    viewPosition == parent.getAdapter().getItemCount() - 1)) {
                if (mDrawableRid != 0) {
                    outRect.set(0, 0, mCurrentThickness, 0);
                } else {
                    outRect.set(0, 0, mThickness, 0);
                }
            }
            if (mFirstLineVisible && viewPosition == 0) {
                if (mDrawableRid != 0) {
                    outRect.set(mCurrentThickness, 0, mCurrentThickness, 0);
                } else {
                    outRect.set(mThickness, 0, mThickness, 0);
                }
            }

        } else if (mMode == MODE_GRID) {
            int columnSize = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            int itemSize = parent.getAdapter().getItemCount();

            if (mDrawableRid != 0) {
                setGridOffsets(outRect, viewPosition, columnSize, itemSize, 0);
            } else {
                setGridOffsets(outRect, viewPosition, columnSize, itemSize, 1);
            }
        }

    }

    /**
     * judge is a color string like #xxxxxx or #xxxxxxxx
     *
     * @param colorStr
     * @return
     */
    public static boolean isColorString(String colorStr) {
        return Pattern.matches("^#([0-9a-fA-F]{6}||[0-9a-fA-F]{8})$", colorStr);
    }

    private boolean isPureLine() {
        if (mDashGap == 0 && mDashWidth == 0)
            return true;
        return false;
    }

    /**
     * draw horizontal decoration
     *
     * @param c
     * @param parent
     */
    private void drawHorinzontal(Canvas c, RecyclerView parent) {
        int childrenCount = parent.getChildCount();
        if (mDrawableRid != 0) {

            if (mFirstLineVisible) {
                View childView = parent.getChildAt(0);
                int myY = childView.getTop();

                if (hasNinePatch) {
                    Rect rect = new Rect(mPaddingStart, myY - mCurrentThickness
                            , parent.getWidth() - mPaddingEnd, myY);
                    mNinePatch.draw(c, rect);
                } else {
                    c.drawBitmap(mBmp, mPaddingStart, myY - mCurrentThickness, mPaint);
                }
            }

            for (int i = 0; i < childrenCount; i++) {
                if (!mLastLineVisible && i == childrenCount - 1)
                    break;
                View childView = parent.getChildAt(i);
                int myY = childView.getBottom();

                if (hasNinePatch) {
                    Rect rect = new Rect(mPaddingStart, myY
                            , parent.getWidth() - mPaddingEnd, myY + mCurrentThickness);
                    mNinePatch.draw(c, rect);
                } else {
                    c.drawBitmap(mBmp, mPaddingStart, myY, mPaint);
                }

            }

        } else {

            boolean isPureLine = isPureLine();
            if (!isPureLine) {
                PathEffect effects = new DashPathEffect(new float[]{0, 0, mDashWidth, mThickness}, mDashGap);
                mPaint.setPathEffect(effects);
            }

            if (mFirstLineVisible) {
                View childView = parent.getChildAt(0);
                int myY = childView.getTop() - mThickness / 2;

                if (isPureLine) {
                    c.drawLine(mPaddingStart, myY, parent.getWidth() - mPaddingEnd, myY, mPaint);
                } else {
                    Path path = new Path();
                    path.moveTo(mPaddingStart, myY);
                    path.lineTo(parent.getWidth() - mPaddingEnd, myY);
                    c.drawPath(path, mPaint);
                }
            }

            for (int i = 0; i < childrenCount; i++) {
                if (!mLastLineVisible && i == childrenCount - 1)
                    break;
                View childView = parent.getChildAt(i);
                int myY = childView.getBottom() + mThickness / 2;

                if (isPureLine) {
                    c.drawLine(mPaddingStart, myY, parent.getWidth() - mPaddingEnd, myY, mPaint);
                } else {
                    Path path = new Path();
                    path.moveTo(mPaddingStart, myY);
                    path.lineTo(parent.getWidth() - mPaddingEnd, myY);
                    c.drawPath(path, mPaint);
                }

            }

        }
    }

    /**
     * draw vertival decoration
     *
     * @param c
     * @param parent
     */
    private void drawVertical(Canvas c, RecyclerView parent) {
        int childrenCount = parent.getChildCount();
        if (mDrawableRid != 0) {

            if (mFirstLineVisible) {
                View childView = parent.getChildAt(0);
                int myX = childView.getLeft();
                if (hasNinePatch) {
                    Rect rect = new Rect(myX - mCurrentThickness, mPaddingStart
                            , myX, parent.getHeight() - mPaddingEnd);
                    mNinePatch.draw(c, rect);
                } else {
                    c.drawBitmap(mBmp, myX - mCurrentThickness, mPaddingStart, mPaint);
                }
            }
            for (int i = 0; i < childrenCount; i++) {
                if (!mLastLineVisible && i == childrenCount - 1)
                    break;
                View childView = parent.getChildAt(i);
                int myX = childView.getRight();
                if (hasNinePatch) {
                    Rect rect = new Rect(myX, mPaddingStart, myX + mCurrentThickness
                            , parent.getHeight() - mPaddingEnd);
                    mNinePatch.draw(c, rect);
                } else {
                    c.drawBitmap(mBmp, myX, mPaddingStart, mPaint);
                }
            }

        } else {

            boolean isPureLine = isPureLine();
            if (!isPureLine) {
                PathEffect effects = new DashPathEffect(new float[]{0, 0, mDashWidth, mThickness}, mDashGap);
                mPaint.setPathEffect(effects);
            }

            if (mFirstLineVisible) {
                View childView = parent.getChildAt(0);
                int myX = childView.getLeft() - mThickness / 2;
                if (isPureLine) {
                    c.drawLine(myX, mPaddingStart, myX, parent.getHeight() - mPaddingEnd, mPaint);
                } else {
                    Path path = new Path();
                    path.moveTo(myX, mPaddingStart);
                    path.lineTo(myX, parent.getHeight() - mPaddingEnd);
                    c.drawPath(path, mPaint);
                }
            }

            for (int i = 0; i < childrenCount; i++) {
                if (!mLastLineVisible && i == childrenCount - 1)
                    break;
                View childView = parent.getChildAt(i);
                int myX = childView.getRight() + mThickness / 2;
                if (isPureLine) {
                    c.drawLine(myX, mPaddingStart, myX, parent.getHeight() - mPaddingEnd, mPaint);
                } else {
                    Path path = new Path();
                    path.moveTo(myX, mPaddingStart);
                    path.lineTo(myX, parent.getHeight() - mPaddingEnd);
                    c.drawPath(path, mPaint);
                }

            }
        }
    }

    /**
     * draw grid decoration
     *
     * @param c
     * @param parent
     */
    private void drawGrid(Canvas c, RecyclerView parent) {

        int childrenCount = parent.getChildCount();
        int columnSize = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        int adapterChildrenCount = parent.getAdapter().getItemCount();

        if (mDrawableRid != 0) {
            if (hasNinePatch) {
                for (int i = 0; i < childrenCount; i++) {
                    View childView = parent.getChildAt(i);

                    //horizontal
                    if (mGridBottomVisible && isLastGridRow(i, adapterChildrenCount, columnSize)) {
                        Rect rect = new Rect(0
                                , childView.getBottom()
                                , childView.getRight() + mBmp.getHeight()
                                , childView.getBottom() + mBmp.getHeight());
                        mNinePatch.draw(c, rect);
                    }

                    if (mGridTopVisible && isFirstGridRow(i, columnSize)) {
                        Rect rect = new Rect(0
                                , 0
                                , childView.getRight() + mBmp.getWidth()
                                , childView.getBottom() + mBmp.getHeight());
                        mNinePatch.draw(c, rect);
                    } else if (!isLastGridRow(i, adapterChildrenCount, columnSize)) {
                        Rect rect = new Rect(
                                childView.getLeft()
                                , childView.getBottom()
                                , childView.getRight()
                                , childView.getBottom() + mBmp.getHeight());
                        mNinePatch.draw(c, rect);

                    }

                    //vertical
                    if (isLastGridRow(i, adapterChildrenCount, columnSize)
                            && !isLastGridColumn(i, childrenCount, columnSize)) {
                        Rect rect = new Rect(
                                childView.getRight()
                                , childView.getTop()
                                , childView.getRight() + mBmp.getWidth()
                                , childView.getBottom());
                        mNinePatch.draw(c, rect);
                    } else if (!isLastGridColumn(i, childrenCount, columnSize)) {
                        Rect rect = new Rect(
                                childView.getRight()
                                , childView.getTop()
                                , childView.getRight() + mBmp.getWidth()
                                , childView.getBottom() + mBmp.getHeight());
                        mNinePatch.draw(c, rect);
                    }

                    if (mGridLeftVisible && isFirstGridColumn(i, columnSize)) {
                        Rect rect = new Rect(
                                childView.getLeft() - mBmp.getWidth()
                                , childView.getTop()
                                , childView.getRight() + mBmp.getWidth()
                                , childView.getBottom() + mBmp.getHeight());
                        mNinePatch.draw(c, rect);
                    }

                    if (mGridRightVisible && isLastGridColumn(i, childrenCount, columnSize)) {
                        Rect rect = new Rect(
                                childView.getRight()
                                , childView.getTop() - mBmp.getHeight()
                                , childView.getRight() + mBmp.getWidth()
                                , childView.getBottom() + mBmp.getHeight());
                        mNinePatch.draw(c, rect);
                    }


                }
            } else {

                for (int i = 0; i < childrenCount; i++) {
                    View childView = parent.getChildAt(i);
                    int myX = childView.getRight();
                    int myY = childView.getBottom();

                    //horizontal
                    if (mGridBottomVisible && isLastGridRow(i, adapterChildrenCount, columnSize)) {
                        c.drawBitmap(mBmp, childView.getLeft(), myY, mPaint);
                    } else if (!isLastGridRow(i, adapterChildrenCount, columnSize)) {
                        c.drawBitmap(mBmp, childView.getLeft(), myY, mPaint);
                    }

                    if (mGridTopVisible && isFirstGridRow(i, columnSize)) {
                        c.drawBitmap(mBmp, childView.getLeft(), childView.getTop() - mBmp.getHeight(), mPaint);
                    }

                    //vertical
                    if (!isLastGridColumn(i, childrenCount, columnSize)) {
                        c.drawBitmap(mBmp, myX, childView.getTop(), mPaint);
                    }

                    if (mGridLeftVisible && isFirstGridColumn(i, columnSize)) {
                        c.drawBitmap(mBmp, childView.getLeft() - mBmp.getWidth(), childView.getTop(), mPaint);
                    }

                    if (mGridRightVisible && isLastGridColumn(i, childrenCount, columnSize)) {
                        c.drawBitmap(mBmp, childView.getRight(), childView.getTop(), mPaint);
                    }

                }
            }
        } else if (mDashWidth == 0 && mDashGap == 0) {

            for (int i = 0; i < childrenCount; i++) {

                View childView = parent.getChildAt(i);
                int myL, myT, myR, myB;

                if (mGridHorizontalSpacing != 0) {
                    myR = childView.getRight() + mGridHorizontalSpacing / 2;
                    myL = childView.getLeft() - mGridHorizontalSpacing / 2;
                } else {
                    myR = childView.getRight() + mThickness / 2;
                    myL = childView.getLeft() - mThickness / 2;
                }

                if (mGridVerticalSpacing != 0) {
                    myB = childView.getBottom() + mGridVerticalSpacing / 2;
                    myT = childView.getTop() - mGridVerticalSpacing / 2;

                } else {
                    myB = childView.getBottom() + mThickness / 2;
                    myT = childView.getTop() - mThickness / 2;
                }

                int layoutPosition = parent.getChildLayoutPosition(childView);

                if (!isLastGridRow(layoutPosition, adapterChildrenCount, columnSize)) {
                    if (mGridVerticalSpacing != 0)
                        mPaint.setStrokeWidth(mGridVerticalSpacing);
                    else
                        mPaint.setStrokeWidth(mThickness);
                    c.drawLine(childView.getLeft(), myB, childView.getRight(), myB, mPaint);
                }
                if (!isFirstGridColumn(layoutPosition, columnSize)) {
                    if (mGridHorizontalSpacing != 0)
                        mPaint.setStrokeWidth(mGridHorizontalSpacing);
                    if (mGridVerticalSpacing != 0) {
                        if (isLastGridRow(layoutPosition, adapterChildrenCount, columnSize))
                            c.drawLine(myL, childView.getTop()
                                    , myL, myB - mGridVerticalSpacing / 2, mPaint);
                        else
                            c.drawLine(myL, childView.getTop()
                                    , myL, myB + mGridVerticalSpacing / 2, mPaint);
                    } else {
                        if (isLastGridRow(layoutPosition, adapterChildrenCount, columnSize))
                            c.drawLine(myL, childView.getTop()
                                    , myL, myB - mThickness / 2, mPaint);
                        else
                            c.drawLine(myL, childView.getTop()
                                    , myL, myB + mThickness / 2, mPaint);

                    }
                }

                if (adapterChildrenCount > columnSize && isLastItem(layoutPosition, adapterChildrenCount)
                        && !isLastGridColumn(layoutPosition, adapterChildrenCount, columnSize)) {

                    if (mGridHorizontalSpacing != 0)
                        mPaint.setStrokeWidth(mGridHorizontalSpacing);
                    if (mGridVerticalSpacing != 0) {
                        c.drawLine(myR, childView.getTop()
                                , myR, myB - mGridVerticalSpacing / 2, mPaint);
                    } else {
                        c.drawLine(myR, childView.getTop()
                                , myR, myB - mThickness / 2, mPaint);
                    }

                }

                if (mGridTopVisible && isFirstGridRow(layoutPosition, columnSize)) {

                    mPaint.setStrokeWidth(mThickness);
                    int tempT = childView.getTop() - mThickness / 2;
                    if (mGridHorizontalSpacing != 0) {
                        if (isLastGridColumn(layoutPosition, adapterChildrenCount, columnSize))
                            c.drawLine(childView.getLeft(), tempT, childView.getRight(), tempT, mPaint);
                        else
                            c.drawLine(childView.getLeft(), tempT
                                    , childView.getRight() + mGridHorizontalSpacing, tempT, mPaint);
                    } else {
                        if (isLastGridColumn(layoutPosition, adapterChildrenCount, columnSize))
                            c.drawLine(childView.getLeft(), tempT, childView.getRight(), tempT, mPaint);
                        else
                            c.drawLine(childView.getLeft(), tempT
                                    , childView.getRight() + mThickness, tempT, mPaint);
                    }

                }

                if (mGridBottomVisible && isLastGridRow(layoutPosition, adapterChildrenCount, columnSize)) {

                    int tempB = childView.getBottom() + mThickness / 2;
                    mPaint.setStrokeWidth(mThickness);
                    if (mGridHorizontalSpacing != 0) {

                        if (isLastGridColumn(layoutPosition, adapterChildrenCount, columnSize))
                            c.drawLine(childView.getLeft(), tempB, childView.getRight(), tempB, mPaint);
                        else
                            c.drawLine(childView.getLeft(), tempB
                                    , childView.getRight() + mGridHorizontalSpacing, tempB, mPaint);

                    } else {
                        if (isLastGridColumn(layoutPosition, adapterChildrenCount, columnSize))
                            c.drawLine(childView.getLeft(), tempB, childView.getRight(), tempB, mPaint);
                        else
                            c.drawLine(childView.getLeft(), tempB
                                    , childView.getRight() + mThickness, tempB, mPaint);
                    }
                }


                if (mGridLeftVisible && isFirstGridColumn(layoutPosition, columnSize)) {
                    mPaint.setStrokeWidth(mThickness);
                    int tempL = childView.getLeft() - mThickness / 2;
                    if (mGridVerticalSpacing != 0) {
                        if (isFirstGridRow(layoutPosition, columnSize)) {
                            c.drawLine(tempL, myT - mGridVerticalSpacing / 2, tempL, childView.getBottom() + mThickness, mPaint);
                        } else {
                            c.drawLine(tempL, myT - mGridVerticalSpacing / 2, tempL, myB + mGridVerticalSpacing / 2, mPaint);
                        }
                    } else {
                        c.drawLine(tempL, myT - mThickness / 2, tempL, myB + mThickness / 2, mPaint);
                    }

                }

                if (mGridRightVisible && isLastGridColumn(layoutPosition, adapterChildrenCount, columnSize)) {

                    mPaint.setStrokeWidth(mThickness);
                    int tempR = childView.getRight() + mThickness / 2;
                    if (mGridVerticalSpacing != 0) {
                        if (isFirstGridRow(layoutPosition, columnSize)) {
                            c.drawLine(tempR, myT - mGridVerticalSpacing / 2
                                    , tempR, childView.getBottom() + mThickness, mPaint);
                        } else {
                            c.drawLine(tempR, myT - mGridVerticalSpacing / 2
                                    , tempR, myB + mGridVerticalSpacing / 2, mPaint);
                        }
                    } else {
                        c.drawLine(tempR, myT - mThickness / 2
                                , tempR, myB + mThickness / 2, mPaint);
                    }
                }
            }

        } else {
            PathEffect effects = new DashPathEffect(new float[]{0, 0, mDashWidth, mThickness}, mDashGap);
            mPaint.setPathEffect(effects);
            for (int i = 0; i < childrenCount; i++) {
                View childView = parent.getChildAt(i);
                int myT, myB, myL, myR;

                if (mGridHorizontalSpacing != 0) {
                    myR = childView.getRight() + mGridHorizontalSpacing / 2;
                    myL = childView.getLeft() - mGridHorizontalSpacing / 2;
                } else {
                    myR = childView.getRight() + mThickness / 2;
                    myL = childView.getLeft() - mThickness / 2;
                }

                if (mGridVerticalSpacing != 0) {
                    myB = childView.getBottom() + mGridVerticalSpacing / 2;
                    myT = childView.getTop() - mGridVerticalSpacing / 2;

                } else {
                    myB = childView.getBottom() + mThickness / 2;
                    myT = childView.getTop() - mThickness / 2;
                }

                int layoutPosition = parent.getChildLayoutPosition(childView);

                if (!isLastGridRow(layoutPosition, adapterChildrenCount, columnSize)) {
                    if (mGridVerticalSpacing != 0)
                        mPaint.setStrokeWidth(mGridVerticalSpacing);
                    else
                        mPaint.setStrokeWidth(mThickness);
                    Path path = new Path();
                    path.moveTo(mThickness, myB);
                    path.lineTo(childView.getRight(), myB);
                    c.drawPath(path, mPaint);
                }

                if (!isFirstGridColumn(layoutPosition, columnSize)) {

                    if (mGridHorizontalSpacing != 0)
                        mPaint.setStrokeWidth(mGridHorizontalSpacing);
                    else
                        mPaint.setStrokeWidth(mThickness);

                    if (isLastGridRow(layoutPosition, adapterChildrenCount, columnSize)) {
                        Path path = new Path();
                        path.moveTo(myL, myT);
                        if (mGridVerticalSpacing != 0) {
                            path.lineTo(myL, myB - mGridVerticalSpacing / 2);
                        } else {
                            path.lineTo(myL, myB - mThickness / 2);
                        }
                        c.drawPath(path, mPaint);
                    } else {
                        Path path = new Path();
                        path.moveTo(myL, myT);
                        path.lineTo(myL, myB);
                        c.drawPath(path, mPaint);
                    }

                }

                if (adapterChildrenCount > columnSize && isLastItem(layoutPosition, adapterChildrenCount)
                        && !isLastGridColumn(layoutPosition, adapterChildrenCount, columnSize)) {

                    if (mGridHorizontalSpacing != 0)
                        mPaint.setStrokeWidth(mGridHorizontalSpacing);
                    else
                        mPaint.setStrokeWidth(mThickness);
                    Path path = new Path();
                    path.moveTo(myR, myT);
                    path.lineTo(myR, myB);
                    c.drawPath(path, mPaint);
                }

                if (mGridTopVisible && isFirstGridRow(layoutPosition, columnSize)) {

                    mPaint.setStrokeWidth(mThickness);

                    int tempT = childView.getTop() - mThickness / 2;
                    Path path = new Path();
                    path.moveTo(childView.getLeft(), tempT);
                    path.lineTo(childView.getRight(), tempT);
                    c.drawPath(path, mPaint);
                }


                if (mGridBottomVisible && isLastGridRow(layoutPosition, adapterChildrenCount, columnSize)) {

                    mPaint.setStrokeWidth(mThickness);

                    int tempB = childView.getBottom() + mThickness / 2;
                    Path path = new Path();
                    path.moveTo(mThickness, tempB);
                    path.lineTo(childView.getRight(), tempB);
                    c.drawPath(path, mPaint);
                }

                if (mGridLeftVisible && isFirstGridColumn(layoutPosition, columnSize)) {

                    mPaint.setStrokeWidth(mThickness);

                    int tempT = childView.getTop() - mThickness / 2;
                    int tempB = childView.getBottom() + mThickness / 2;
                    int tempL = childView.getLeft() - mThickness / 2;
                    Path path = new Path();
                    path.moveTo(tempL, tempT);
                    path.lineTo(tempL, tempB);
                    c.drawPath(path, mPaint);

                }

                if (mGridRightVisible && isLastGridColumn(layoutPosition, adapterChildrenCount, columnSize)) {

                    mPaint.setStrokeWidth(mThickness);
                    int tempT = childView.getTop() - mThickness / 2;
                    int tempB = childView.getBottom() + mThickness / 2;
                    int tempR = childView.getRight() + mThickness / 2;
                    Path path = new Path();
                    path.moveTo(tempR, tempT);
                    path.lineTo(tempR, tempB);
                    c.drawPath(path, mPaint);
                }

            }
        }
    }

    /**
     * set offsets for grid mode
     *
     * @param outRect
     * @param viewPosition
     * @param columnSize
     * @param itemSize
     * @param tag          0 for drawable
     */
    public void setGridOffsets(Rect outRect, int viewPosition, int columnSize
            , int itemSize, int tag) {

        int x;
        int y;
        int borderThickness = mThickness;
        if (tag == 0) {
            x = mBmp.getWidth();
            y = mBmp.getHeight();
        } else {

            if (mGridHorizontalSpacing != 0)
                x = mGridHorizontalSpacing;
            else
                x = mThickness;

            if (mGridVerticalSpacing != 0)
                y = mGridVerticalSpacing;
            else
                y = mThickness;


        }
        if (isFirstGridColumn(viewPosition, columnSize)
                && isFirstGridRow(viewPosition, columnSize)) {

            if (mGridTopVisible && mGridLeftVisible) {
                outRect.set(borderThickness, borderThickness, 0, 0);
            } else if (mGridTopVisible) {
                outRect.set(0, borderThickness, 0, 0);
            } else if (mGridLeftVisible) {
                outRect.set(borderThickness, 0, 0, 0);
            } else {
                outRect.set(0, 0, 0, 0);
                outRect.set(0, 0, 0, 0);
            }
        } else if (isFirstGridRow(viewPosition, columnSize)
                && isLastGridColumn(viewPosition, itemSize, columnSize)) {
            if (mGridTopVisible && mGridRightVisible) {
                outRect.set(x, borderThickness, borderThickness, 0);
            } else if (mGridTopVisible) {
                outRect.set(x, borderThickness, 0, 0);
            } else if (mGridRightVisible) {
                outRect.set(x, 0, borderThickness, 0);
            } else {
                outRect.set(x, 0, 0, 0);
            }
        } else if (isFirstGridColumn(viewPosition, columnSize)
                && isLastGridRow(viewPosition, itemSize, columnSize)) {
            if (mGridLeftVisible && mGridBottomVisible) {
                outRect.set(borderThickness, y, 0, borderThickness);
            } else if (mGridLeftVisible) {
                outRect.set(borderThickness, y, 0, 0);
            } else if (mGridBottomVisible) {
                outRect.set(0, y, 0, borderThickness);
            } else {
                outRect.set(0, y, 0, 0);
            }
        } else if (isLastGridColumn(viewPosition, itemSize, columnSize)
                && isLastGridRow(viewPosition, itemSize, columnSize)) {
            if (mGridRightVisible && mGridBottomVisible) {
                outRect.set(x, y, borderThickness, borderThickness);
            } else if (mGridRightVisible) {
                outRect.set(x, y, borderThickness, 0);
            } else if (mGridBottomVisible) {
                outRect.set(x, y, 0, borderThickness);
            } else {
                outRect.set(x, y, 0, 0);
            }
        } else if (isFirstGridRow(viewPosition, columnSize)) {
            if (mGridTopVisible) {
                outRect.set(x, borderThickness, 0, 0);
            } else {
                outRect.set(x, 0, 0, 0);
            }
        } else if (isFirstGridColumn(viewPosition, columnSize)) {

            if (mGridLeftVisible) {
                outRect.set(borderThickness, y, 0, 0);
            } else {
                outRect.set(0, y, 0, 0);
            }
        } else if (isLastGridColumn(viewPosition, itemSize, columnSize)) {
            if (mGridRightVisible) {
                outRect.set(x, y, borderThickness, 0);
            } else {
                outRect.set(x, y, 0, 0);
            }
        } else if (isLastGridRow(viewPosition, itemSize, columnSize)) {

            if (mGridBottomVisible) {
                outRect.set(x, y, 0, borderThickness);
            } else {
                outRect.set(x, y, 0, 0);
            }
        } else {
            outRect.set(x, y, 0, 0);
        }
    }

    /**
     * check if is one of the first columns
     *
     * @param position
     * @param columnSize
     * @return
     */
    private boolean isFirstGridColumn(int position, int columnSize) {

        return position % columnSize == 0;
    }

    /**
     * check if is one of the last columns
     *
     * @param position
     * @param columnSize
     * @return
     */
    private boolean isLastGridColumn(int position, int itemSize, int columnSize) {
        boolean isLast = false;
        if (((position + 1) % columnSize == 0) || (itemSize <= columnSize && position == itemSize - 1)) {
            isLast = true;
        }
        return isLast;
    }

    /**
     * check if is the first row of th grid
     *
     * @param position
     * @param columnSize
     * @return
     */
    private boolean isFirstGridRow(int position, int columnSize) {
        return position < columnSize;
    }

    /**
     * check if is the last row of the grid
     *
     * @param position
     * @param itemSize
     * @param columnSize
     * @return
     */
    private boolean isLastGridRow(int position, int itemSize, int columnSize) {
        int temp = itemSize % columnSize;
        if (temp == 0 && position >= itemSize - columnSize) {
            return true;
        } else if (position >= itemSize / columnSize * columnSize) {
            return true;
        }
        return false;
    }

    /**
     * check if is the last item
     *
     * @param position
     * @param itemSize
     * @return
     */
    private boolean isLastItem(int position, int itemSize) {
        return position == itemSize - 1;
    }

    /**
     * compatible with recyclerview layoutmanager
     *
     * @param parent
     */
    private void compatibleWithLayoutManager(RecyclerView parent) {

        if (parent.getLayoutManager() != null) {
            if (parent.getLayoutManager() instanceof GridLayoutManager) {
                mMode = MODE_GRID;
            } else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
                if (((LinearLayoutManager) parent.getLayoutManager()).getOrientation() == LinearLayout.HORIZONTAL) {
                    mMode = MODE_VERTICAL;
                } else {
                    mMode = MODE_HORIZONTAL;
                }
            }
        }
    }

    public static class Builder {

        private Param params;
        private Context context;

        public Builder(Context context) {

            params = new Param();
            this.context = context;

        }

        public RecyclerViewItemDecoration create() {
            RecyclerViewItemDecoration recyclerViewItemDecoration = new RecyclerViewItemDecoration();
            recyclerViewItemDecoration.setParams(context, params);
            return recyclerViewItemDecoration;
        }

        public Builder mode(int mode) {
            params.mode = mode;
            return this;
        }

        public Builder drawableID(int drawableID) {
            params.drawableRid = drawableID;
            return this;
        }

        public Builder color(@ColorInt int color) {
            params.color = color;
            return this;
        }

        public Builder color(String color) {
            if (isColorString(color)) {
                params.color = Color.parseColor(color);
            }
            return this;
        }

        public Builder thickness(int thickness) {
            params.thickness = thickness;
            return this;
        }

        public Builder dashWidth(int dashWidth) {
            params.dashWidth = dashWidth;
            return this;
        }

        public Builder dashGap(int dashGap) {
            params.dashGap = dashGap;
            return this;
        }

        public Builder lastLineVisible(boolean visible) {
            params.lastLineVisible = visible;
            return this;
        }

        public Builder firstLineVisible(boolean visible) {
            params.firstLineVisible = visible;
            return this;
        }

        public Builder paddingStart(int padding) {
            params.paddingStart = padding;
            return this;
        }

        public Builder paddingEnd(int padding) {
            params.paddingEnd = padding;
            return this;
        }

        public Builder gridLeftVisible(boolean visible) {
            params.gridLeftVisible = visible;
            return this;
        }

        public Builder gridRightVisible(boolean visible) {
            params.gridRightVisible = visible;
            return this;
        }

        public Builder gridTopVisible(boolean visible) {
            params.gridTopVisible = visible;
            return this;
        }

        public Builder gridBottomVisible(boolean visible) {
            params.gridBottomVisible = visible;
            return this;
        }

        public Builder gridHorizontalSpacing(int spacing) {
            params.gridHorizontalSpacing = spacing;
            return this;
        }

        public Builder gridVerticalSpacing(int spacing) {
            params.gridVerticalSpacing = spacing;
            return this;
        }


        public Builder parent(RecyclerView recyclerView) {
            params.parent = recyclerView;
            return this;
        }

    }

    private static class Param {

        public int mode = MODE_HORIZONTAL;
        public int drawableRid = 0;
        public int color = Color.parseColor(DEFAULT_COLOR);
        public int thickness;
        public int dashWidth = 0;
        public int dashGap = 0;
        public boolean lastLineVisible;
        public boolean firstLineVisible;
        public int paddingStart;
        public int paddingEnd;
        public boolean gridLeftVisible;
        public boolean gridRightVisible;
        public boolean gridTopVisible;
        public boolean gridBottomVisible;
        public int gridHorizontalSpacing;
        public int gridVerticalSpacing;
        public RecyclerView parent;
    }

}