package com.zchu.reader;

import android.graphics.Paint;

/**
 * Created by Chu on 2017/8/13.
 */

public final class PageProperty {

   // private int hash;

    /**
     * 画笔
     */
    public Paint textPaint;

    /**
     * 文本绘制区域的高
     */
    public int visibleHeight;

    /**
     * 文本绘制区域的宽
     */
    public int visibleWidth;

    /**
     * 行间距
     */
    public int intervalSize;

    /**
     * 段落间距(基于行间距的额外距离)
     */
    public int paragraphSize;

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
      /*  int h = hash;
        if (h == 0) {


            hash = h;
        }*/
        return (int) (textPaint.getTextSize()+visibleHeight+visibleWidth+intervalSize+paragraphSize);
    }
}
