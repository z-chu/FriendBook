package com.zchu.reader;

import android.graphics.Paint;
import android.util.LruCache;
import android.util.SparseArray;

import com.zchu.reader.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chu on 2017/8/12.
 */

public abstract class StringAdapter implements PageLoaderAdapter {
    private LruCache<Integer, SparseArray<ArrayList<String>>> map;
    private PageProperty mPageProperty;


    @Override
    public int getPageCount(int section, PageProperty property) {
        return getSectionData(section,property).size();
    }

    @Override
    public List<String> getPageLines(int section, int page, PageProperty property) {
        return getSectionData(section,property).get(page);
    }

    private SparseArray<ArrayList<String>> getSectionData(int section, PageProperty property) {
        SparseArray<ArrayList<String>> pages = null;
        if (map == null) {
            map = new LruCache<>(3);
            pages = loadPages(getPageSource(section), property.textPaint, property.visibleHeight, property.visibleWidth, property.intervalSize, property.paragraphSize);
            map.put(section, pages);
            mPageProperty=property;
        } else {
            if (mPageProperty != null && mPageProperty.equals(property)) {
                pages = map.get(section);
            }
            if (pages == null) {
                pages = loadPages(getPageSource(section), property.textPaint, property.visibleHeight, property.visibleWidth, property.intervalSize, property.paragraphSize);
                map.put(section, pages);
                mPageProperty=property;
            }
        }
        return pages;
    }


    protected abstract String getPageSource(int section);

    @Override
    public abstract int getSectionCount();

    @Override
    public abstract String getSectionName(int section);

    public static SparseArray<ArrayList<String>> loadPages(String source, Paint textPaint, int visibleHeight, int visibleWidth, int intervalSize, int paragraphSize) {
        SparseArray<ArrayList<String>> pageArray = new SparseArray<>();
        List<String> lines = new ArrayList<>();
        if (source != null && source.length() > 0) {
            String[] split = source.split("\n");
            //剩余高度
            int rHeight = visibleHeight + intervalSize + paragraphSize;
            for (String paragraph : split) {
                boolean hasContent=false;
                //如果只有换行符，那么就不执行
                if (StringUtils.isBlank(paragraph)) continue;
                //重置段落
                paragraph = StringUtils.halfToFull("  " + paragraph + "\n");
                paragraph = StringUtils.trimBeforeReplace(paragraph, "　　");
                while (paragraph.length() > 0) {


                    //测量一行占用的字节数
                    int count = textPaint.breakText(paragraph, true, visibleWidth, null);
                    String subStr = paragraph.substring(0, count);
                    String trim = subStr.trim();
                    if (trim.length()>0&&!trim.equals("\n") && !trim.equals("\r\n")&&!StringUtils.isBlank(trim)) {
                        //重置剩余距离
                        rHeight -= (textPaint.getTextSize() + intervalSize);

                        //达到行数要求,创建Page
                        if (rHeight < 0) {
                            //创建Page
                            pageArray.put(pageArray.size(), new ArrayList<>(lines));
                            //重置Lines
                            lines.clear();
                            rHeight = visibleHeight;
                            continue;
                        }
                        //将一行字节，存储到lines中
                        lines.add(subStr);
                        hasContent=true;
                    }


                    //裁剪
                    paragraph = paragraph.substring(count);
                }

                if (lines.size() > 0&&hasContent) {
                    rHeight -= paragraphSize;
                }
            }

            if (lines.size() != 0) {
                pageArray.put(pageArray.size(), new ArrayList<>(lines));
                //重置Lines
                lines.clear();

            }
        }
        return pageArray;
    }
}
