package com.zchu.reader;

import android.graphics.Paint;

/**
 * Created by Chu on 2017/8/13.
 */

public final class PageProperty {

    public PageProperty(Paint textPaint, int visibleWidth, int visibleHeight, int intervalSize, int paragraphSize) {
        this.textPaint = textPaint;
        this.visibleHeight = visibleHeight;
        this.visibleWidth = visibleWidth;
        this.intervalSize = intervalSize;
        this.paragraphSize = paragraphSize;
    }

    private int hash;

    /**
     * 画笔
     */
    public final Paint textPaint;

    /**
     * 文本绘制区域的高
     */
    public final int visibleHeight;

    /**
     * 文本绘制区域的宽
     */
    public final int visibleWidth;

    /**
     * 行间距
     */
    public final int intervalSize;

    /**
     * 段落间距(基于行间距的额外距离)
     */
    public final int paragraphSize;


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof PageProperty) {
            PageProperty anotherPageProperty = (PageProperty) obj;
            if (anotherPageProperty.textPaint != null && this.textPaint != null) {
                if (anotherPageProperty.textPaint.getTextSize() != this.textPaint.getTextSize()) {
                    return false;
                }
            }
            if (!(anotherPageProperty.textPaint == null && this.textPaint == null)) {
                return false;
            }
            if (anotherPageProperty.visibleHeight != this.visibleHeight) {
                return false;
            }
            if (anotherPageProperty.visibleWidth != this.visibleWidth) {
                return false;
            }
            if (anotherPageProperty.intervalSize != this.intervalSize) {
                return false;
            }
            if (anotherPageProperty.paragraphSize != this.paragraphSize) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        if (hash == 0) {
            int result = 20;
            result = (int) (result * 31 + textPaint.getTextSize());
            result = result * 31 + visibleHeight;
            result = result * 31 + visibleWidth;
            result = result * 31 + intervalSize;
            result = result * 31 + visibleHeight;
            result = result * 31 + paragraphSize;
            hash = result;
        }
        return hash;
    }
}
