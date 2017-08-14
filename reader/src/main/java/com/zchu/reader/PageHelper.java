package com.zchu.reader;

import android.graphics.Paint;

import com.zchu.reader.utils.IOUtils;
import com.zchu.reader.utils.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chu on 2017/8/12.
 */

public class PageHelper {

    //

    /**
     * 通过流获取Page的方法
     *
     * @param source        字符输入流
     * @param textPaint     画笔
     * @param visibleHeight 文本绘制区域的高
     * @param visibleWidth  文本绘制区域的宽
     * @param intervalSize  行间距
     * @param paragraphSize 段落距离(基于行间距的额外距离)
     * @return 用于绘制的TxtPage集合
     */
    public static List<TxtPage> loadPages(BufferedReader source, Paint textPaint, int visibleHeight, int visibleWidth, int intervalSize, int paragraphSize) {
        //读取数据段
        List<TxtPage> pages = new ArrayList<>();
        //使用流的方式加载
        List<String> lines = new ArrayList<>();
        //剩余高度
        int rHeight = visibleHeight + intervalSize + paragraphSize;

        String paragraph = null;
        try {
            while ((paragraph = source.readLine()) != null) {
                paragraph = paragraph.replaceAll("\\s", "");
                //如果只有换行符，那么就不执行
                if (StringUtils.isBlank(paragraph)) continue;
                //重置段落
                paragraph = StringUtils.halfToFull("  " + paragraph + "\n");

                while (paragraph.length() > 0) {
                    //重置剩余距离
                    rHeight -= (textPaint.getTextSize() + intervalSize);

                    //达到行数要求,创建Page
                    if (rHeight <= 0) {
                        //创建Page
                        TxtPage page = new TxtPage();
                        page.position = pages.size();
                        page.lines = new ArrayList<>(lines);
                        pages.add(page);
                        //重置Lines
                        lines.clear();
                        rHeight = visibleHeight;
                        continue;
                    }
                    //测量一行占用的字节数
                    int count = textPaint.breakText(paragraph, true, visibleWidth, null);
                    String subStr = paragraph.substring(0, count);
                    if (!subStr.equals("\n") || !subStr.equals("\r\n")) {
                        //将一行字节，存储到lines中
                        lines.add(paragraph.substring(0, count));
                    }
                    //裁剪
                    paragraph = paragraph.substring(count);
                }

                if (lines.size() != 0) {
                    rHeight -= paragraphSize;
                }
            }

            if (lines.size() != 0) {
                //创建Page
                TxtPage page = new TxtPage();
                page.position = pages.size();
                page.lines = new ArrayList<>(lines);
                pages.add(page);
                //重置Lines
                lines.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(source);
        }
        return pages;
    }

    /**
     * 通过流获取Page的方法
     *
     * @param source        文字内容
     * @param textPaint     画笔
     * @param visibleHeight 文本绘制区域的高
     * @param visibleWidth  文本绘制区域的宽
     * @param intervalSize  行间距
     * @param paragraphSize 段落距离(基于行间距的额外距离)
     * @return 用于绘制的TxtPage集合
     */
    public static List<TxtPage> loadPages(String source, Paint textPaint, int visibleHeight, int visibleWidth, int intervalSize, int paragraphSize) {
        //读取数据段
        List<TxtPage> pages = new ArrayList<>();
        //使用流的方式加载
        List<String> lines = new ArrayList<>();
        if (source != null && source.length() > 0) {
            String[] split = source.split("\n");
            //剩余高度
            int rHeight = visibleHeight + intervalSize + paragraphSize;
            for (String paragraph : split) {
                // paragraph = source.replaceAll("\\s", "");
                //如果只有换行符，那么就不执行
                if (StringUtils.isBlank(paragraph)) continue;
                //重置段落
                paragraph = StringUtils.halfToFull("  " + paragraph + "\n");
               paragraph = StringUtils.trimBeforeReplace(paragraph,"　　");
                while (paragraph.length() > 0) {
                    //重置剩余距离
                    rHeight -= (textPaint.getTextSize() + intervalSize);

                    //达到行数要求,创建Page
                    if (rHeight <= 0) {
                        //创建Page
                        TxtPage page = new TxtPage();
                        page.position = pages.size();
                        page.lines = new ArrayList<>(lines);
                        pages.add(page);
                        //重置Lines
                        lines.clear();
                        rHeight = visibleHeight;
                        continue;
                    }
                    //测量一行占用的字节数
                    int count = textPaint.breakText(paragraph, true, visibleWidth, null);
                    String subStr = paragraph.substring(0, count);
                    if (!subStr.equals("\n") || !subStr.equals("\r\n")) {
                        //将一行字节，存储到lines中
                        lines.add(paragraph.substring(0, count));
                    }
                    //裁剪
                    paragraph = paragraph.substring(count);
                }

                if (lines.size() != 0) {
                    rHeight -= paragraphSize;
                }
            }

            if (lines.size() != 0) {
                //创建Page
                TxtPage page = new TxtPage();
                page.position = pages.size();
                page.lines = new ArrayList<>(lines);
                pages.add(page);
                //重置Lines
                lines.clear();
            }
        }
        return pages;
    }
}
