package com.zchu.reader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;

import com.zchu.reader.utils.ScreenUtils;
import com.zchu.reader.utils.StringUtils;
import com.zchu.reader.utils.ToastUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by newbiechen on 17-7-1.
 */

class PageLoader {
    private static final String TAG = "PageLoader";

    //当前页面的状态
    public static final int STATUS_LOADING = 1;  //正在加载
    public static final int STATUS_FINISH = 2;   //加载完成
    public static final int STATUS_ERROR = 3;    //加载错误 (一般是网络加载情况)
    public static final int STATUS_EMPTY = 4;    //空数据
    public static final int STATUS_PARSE = 5;    //正在解析 (一般用于本地数据加载)
    public static final int STATUS_PARSE_ERROR = 6; //本地文件解析错误(暂未被使用)

    static final int DEFAULT_MARGIN_HEIGHT = 30;
    static final int DEFAULT_MARGIN_WIDTH = 16;

    //默认的显示参数配置
    private static final int DEFAULT_INTERVAL = 12;
    private static final int DEFAULT_PARAGRAPH_INTERVAL = 10;

    private static final int DEFAULT_TIP_SIZE = 12;

    //监听器
    protected OnPageChangeListener mPageChangeListener;

    //页面显示类
    private PageView mPageView;


    //绘制电池的画笔
    private Paint mBatteryPaint;
    //绘制提示的画笔
    private Paint mTipPaint;
    //绘制背景颜色的画笔(用来擦除需要重绘的部分)
    private Paint mBgPaint;
    //绘制小说内容的画笔
    private TextPaint mTextPaint;
    //当前的状态
    protected int mStatus = STATUS_LOADING;

    //书籍绘制区域的宽高
    private int mVisibleWidth;
    private int mVisibleHeight;
    //应用的宽高
    private int mDisplayWidth;
    private int mDisplayHeight;
    //间距
    private int mMarginWidth;
    private int mMarginHeight;
    //行间距
    private int mIntervalSize;
    //段落距离(基于行间距的额外距离)
    private int mParagraphSize;
    //电池的百分比
    private int mBatteryLevel;
    //页面的翻页效果模式
    private int mPageMode = PageView.PAGE_MODE_SIMULATION;

    protected Context mContext;

    protected PageLoaderAdapter mAdapter;

    //被遮盖的页，或者认为被取消显示的页
    private TxtPage mCancelPage;
    //当前显示的页
    private TxtPage mCurPage;
    //上一章的页面列表缓存
    private WeakReference<List<TxtPage>> mWeakPrePageList;
    //当前章节的页面列表
    private List<TxtPage> mCurPageList;
    //下一章的页面列表缓存
    private List<TxtPage> mNextPageList;
    //上一章的记录
    private int mLastChapter = 0;
    //当前章
    protected int mCurChapterPos = 0;
    //书本是否打开
    protected boolean isBookOpen = false;


    public PageLoader(PageView pageView) {
        mPageView = pageView;
        mContext = pageView.getContext();
        //初始化数据
        initData();
        //初始化画笔
        initPaint();
        //初始化PageView
        initPageView();


    }

    public void setAdapter(PageLoaderAdapter adapter) {
        this.mAdapter = adapter;
    }

    public PageLoaderAdapter getAdapter() {
        return mAdapter;
    }

    private void initData() {
        //初始化参数
        mMarginWidth = ScreenUtils.dpToPx(mContext, DEFAULT_MARGIN_WIDTH);
        mMarginHeight = ScreenUtils.dpToPx(mContext, DEFAULT_MARGIN_HEIGHT);
        mIntervalSize = ScreenUtils.dpToPx(mContext, DEFAULT_INTERVAL);
        mParagraphSize = ScreenUtils.dpToPx(mContext, DEFAULT_PARAGRAPH_INTERVAL);
    }

    private void initPaint() {
        //绘制提示的画笔
        mTipPaint = new Paint();
        mTipPaint.setColor(mPageView.getTextColor());
        mTipPaint.setTextAlign(Paint.Align.LEFT);//绘制的起始点
        mTipPaint.setTextSize(ScreenUtils.spToPx(mContext, DEFAULT_TIP_SIZE));//Tip默认的字体大小
        mTipPaint.setAntiAlias(true);
        mTipPaint.setSubpixelText(true);

        //绘制页面内容的画笔
        mTextPaint = new TextPaint();
        mTextPaint.setColor(mPageView.getTextColor());
        mTextPaint.setTextSize(mPageView.getTextSize());
        mTextPaint.setAntiAlias(true);

        mBgPaint = new Paint();
        mBgPaint.setColor(mPageView.getPageBackground());

        mBatteryPaint = new Paint();
        mBatteryPaint.setAntiAlias(true);
        mBatteryPaint.setDither(true);
        mBatteryPaint.setColor(mPageView.getTextColor());

    }

    private void initPageView() {
        //配置参数
        mPageView.setPageMode(mPageMode);
        //  mPageView.setBgColor(mPageBg);
    }

    /****************************** public method***************************/
    //跳转到上一章
    public int skipPreChapter() {
        if (!isBookOpen) {
            return mCurChapterPos;
        }

        //载入上一章。
        if (prevChapter()) {
            mCurPage = getCurPage(0);
            mPageView.refreshPage();
        }
        return mCurChapterPos;
    }

    //跳转到下一章
    public int skipNextChapter() {
        if (!isBookOpen) {
            return mCurChapterPos;
        }

        //判断是否达到章节的终止点
        if (nextChapter()) {
            mCurPage = getCurPage(0);
            mPageView.refreshPage();
        }
        return mCurChapterPos;
    }

    //跳转到指定章节
    public void skipToChapter(int pos) {
        //正在加载
        mStatus = STATUS_LOADING;
        //绘制当前的状态
        mCurChapterPos = pos;
        //将上一章的缓存设置为null
        mWeakPrePageList = null;

        //如果当前下一章缓存正在执行，则取消
        /*if (mPreLoadDisp != null) {
            mPreLoadDisp.dispose();
        }*/
        //将下一章缓存设置为null
        mNextPageList = null;

        if (mPageChangeListener != null) {
            mPageChangeListener.onChapterChange(mCurChapterPos);
        }

        if (mCurPage != null) {
            //重置position的位置，防止正在加载的时候退出时候存储的位置为上一章的页码
            mCurPage.position = 0;
        }

        //需要对ScrollAnimation进行重新布局
        mPageView.refreshPage();
    }

    //跳转到具体的页
    public void skipToPage(int pos) {
        mCurPage = getCurPage(pos);
        mPageView.refreshPage();
    }

    //自动翻到上一章
    public boolean autoPrevPage() {
        if (!isBookOpen) return false;
        return mPageView.autoPrevPage();
    }

    //自动翻到下一章
    public boolean autoNextPage() {
        if (!isBookOpen) return false;
        return mPageView.autoNextPage();
    }

    //更新时间
    public void updateTime() {
        if (mPageView.isPrepare() && !mPageView.isRunning()) {
            mPageView.drawCurPage(true);
        }
    }

    //更新电量
    public void updateBattery(int level) {
        mBatteryLevel = level;
        if (mPageView.isPrepare() && !mPageView.isRunning()) {
            mPageView.drawCurPage(true);
        }
    }

    //设置文字大小
    public void setTextSize(int textSize) {
        //设置画笔的字体大小
        mTextPaint.setTextSize(textSize);
        if (!isBookOpen) return;
        //存储状态
        //mSettingManager.setTextSize(mTextSize);
        //取消缓存
        mWeakPrePageList = null;
        mNextPageList = null;
        //如果当前为完成状态。
        if (mStatus == STATUS_FINISH) {
            //重新计算页面
            // mCurPageList = loadPageList(mCurChapterPos);
            int pageCount = mAdapter.getPageCount(mCurChapterPos, new PageProperty(mTextPaint, mVisibleWidth, mVisibleHeight, mIntervalSize, mParagraphSize));
            if (pageCount > 0) {
                List<TxtPage> txtPages = new ArrayList<>();
                for (int i = 0; i < pageCount; i++) {
                    txtPages.add(new TxtPage(i, mAdapter.getPageLines(mCurChapterPos, i, new PageProperty(mTextPaint, mVisibleWidth, mVisibleHeight, mIntervalSize, mParagraphSize))));
                }
                mCurPageList = txtPages;
            }

            //防止在最后一页，通过修改字体，以至于页面数减少导致崩溃的问题
            if (mCurPage.position >= mCurPageList.size()) {
                mCurPage.position = mCurPageList.size() - 1;
            }
        }
        //重新设置文章指针的位置
        mCurPage = getCurPage(mCurPage.position);
        //绘制
        mPageView.refreshPage();
    }

    //翻页动画
    public void setPageMode(int pageMode) {
        mPageMode = pageMode;
        mPageView.setPageMode(mPageMode);
        //重绘
        mPageView.drawCurPage(false);
    }

    //设置页面切换监听
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mPageChangeListener = listener;
    }

    //获取当前页的状态
    public int getPageStatus() {
        return mStatus;
    }

    //获取当前章节的章节位置
    public int getChapterPos() {
        return mCurChapterPos;
    }

    //获取当前页的页码
    public int getPagePos() {
        return mCurPage.position;
    }

    //TODO 保存阅读记录
    public void saveRecord() {
        //书没打开，就没有记录
        if (!isBookOpen) return;

   /*     mBookRecord.setBookId(mCollBook.get_id());
        mBookRecord.setChapter(mCurChapterPos);
        mBookRecord.setPagePos(mCurPage.position);

        //存储到数据库
        BookRepository.getInstance()
                .saveBookRecord(mBookRecord);*/
    }


    //打开具体章节
    public void openChapter() {
        // mCurPageList = loadPageList(mCurChapterPos);
        int pageCount = mAdapter.getPageCount(mCurChapterPos, new PageProperty(mTextPaint, mVisibleWidth, mVisibleHeight, mIntervalSize, mParagraphSize));
        if (pageCount > 0) {
            List<TxtPage> txtPages = new ArrayList<>();
            for (int i = 0; i < pageCount; i++) {
                txtPages.add(new TxtPage(i, mAdapter.getPageLines(mCurChapterPos, i, new PageProperty(mTextPaint, mVisibleWidth, mVisibleHeight, mIntervalSize, mParagraphSize))));
            }
            mCurPageList = txtPages;
        }
        //进行预加载
        preLoadNextChapter();
        //加载完成
        mStatus = STATUS_FINISH;
        //获取制定页面
        if (!isBookOpen) {
            isBookOpen = true;
            //TODO 可能会出现当前页的大小大于记录页的情况。
           /* int position = mBookRecord.getPagePos();
            if (position >= mCurPageList.size()) {
                position = mCurPageList.size() - 1;
            }
            mCurPage = getCurPage(position);*/
            mCurPage = getCurPage(0);
            if (mPageChangeListener != null) {
                mPageChangeListener.onChapterChange(mCurChapterPos);
            }
        } else {
            mCurPage = getCurPage(0);
        }

        mPageView.drawCurPage(false);
    }

    public void openChapter(int section) {
        openChapter(section,0);
    }

    public void openChapter(int section, int page) {
        mCurChapterPos = section;
        int pageCount = mAdapter.getPageCount(mCurChapterPos, new PageProperty(mTextPaint, mVisibleWidth, mVisibleHeight, mIntervalSize, mParagraphSize));
        if (page >= pageCount) {
            page = pageCount - 1;
        }
        if (pageCount > 0) {
            List<TxtPage> txtPages = new ArrayList<>();
            for (int i = 0; i < pageCount; i++) {
                txtPages.add(new TxtPage(i, mAdapter.getPageLines(mCurChapterPos, i, new PageProperty(mTextPaint, mVisibleWidth, mVisibleHeight, mIntervalSize, mParagraphSize))));
            }
            mCurPageList = txtPages;
        }
        //进行预加载
        preLoadNextChapter();
        //加载完成
        mStatus = STATUS_FINISH;
        //获取制定页面
        if (!isBookOpen) {
            isBookOpen = true;
            mCurPage = getCurPage(page);
            if (mPageChangeListener != null) {
                mPageChangeListener.onChapterChange(mCurChapterPos);
            }
        } else {
            mCurPage = getCurPage(page);
        }

        mPageView.drawCurPage(false);
    }

    public void chapterError() {
        //加载错误
        mStatus = STATUS_ERROR;
        //显示加载错误
        mPageView.drawCurPage(false);
    }


    void onDraw(Bitmap bitmap, boolean isUpdate) {
        //如果是上下滑动
        drawBackground(mPageView.getBgBitmap(), isUpdate);

        if (!isUpdate) {
            drawContent(bitmap);
        }
        //更新绘制
        mPageView.invalidate();
    }

    void drawBackground(Bitmap bitmap, boolean isUpdate) {
        Canvas canvas = new Canvas(bitmap);
        int tipMarginHeight = ScreenUtils.dpToPx(mContext, 3);
        if (!isUpdate) {
            /****绘制背景****/
            canvas.drawColor(mPageView.getPageBackground());

            /*****初始化标题的参数********/
            //需要注意的是:绘制text的y的起始点是text的基准线的位置，而不是从text的头部的位置
            float tipTop = tipMarginHeight - mTipPaint.getFontMetrics().top;
            //根据状态不一样，数据不一样
            if (mStatus != STATUS_FINISH) {
                if (mAdapter != null && mAdapter.getSectionCount() != 0) {
                    canvas.drawText(mAdapter.getSectionName(mCurChapterPos)
                            , mMarginWidth, tipTop, mTipPaint);
                }
            } else {
                canvas.drawText(mAdapter.getSectionName(mCurChapterPos), mMarginWidth, tipTop, mTipPaint);
            }

            /******绘制页码********/
            //底部的字显示的位置Y
            float y = mDisplayHeight - mTipPaint.getFontMetrics().bottom - tipMarginHeight;
            //只有finish的时候采用页码
            if (mStatus == STATUS_FINISH) {
                String percent = (mCurPage.position + 1) + "/" + mCurPageList.size();
                canvas.drawText(percent, mMarginWidth, y, mTipPaint);
            }
        } else {
            //擦除区域
            mBgPaint.setColor(mPageView.getPageBackground());
            canvas.drawRect(mDisplayWidth / 2, mDisplayHeight - mMarginHeight + ScreenUtils.dpToPx(mContext, 2), mDisplayWidth, mDisplayHeight, mBgPaint);
        }
        /******绘制电池********/

        int visibleRight = mDisplayWidth - mMarginWidth;
        int visibleBottom = mDisplayHeight - tipMarginHeight;

        int outFrameWidth = (int) mTipPaint.measureText("xxx");
        int outFrameHeight = (int) mTipPaint.getTextSize();

        int polarHeight = ScreenUtils.dpToPx(mContext, 6);
        int polarWidth = ScreenUtils.dpToPx(mContext, 2);
        int border = 1;
        int innerMargin = 1;

        //电极的制作
        int polarLeft = visibleRight - polarWidth;
        int polarTop = visibleBottom - (outFrameHeight + polarHeight) / 2;
        Rect polar = new Rect(polarLeft, polarTop, visibleRight,
                polarTop + polarHeight - ScreenUtils.dpToPx(mContext, 2));

        mBatteryPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(polar, mBatteryPaint);

        //外框的制作
        int outFrameLeft = polarLeft - outFrameWidth;
        int outFrameTop = visibleBottom - outFrameHeight;
        int outFrameBottom = visibleBottom - ScreenUtils.dpToPx(mContext, 2);
        Rect outFrame = new Rect(outFrameLeft, outFrameTop, polarLeft, outFrameBottom);

        mBatteryPaint.setStyle(Paint.Style.STROKE);
        mBatteryPaint.setStrokeWidth(border);
        canvas.drawRect(outFrame, mBatteryPaint);

        //内框的制作
        float innerWidth = (outFrame.width() - innerMargin * 2 - border) * (mBatteryLevel / 100.0f);
        RectF innerFrame = new RectF(outFrameLeft + border + innerMargin, outFrameTop + border + innerMargin,
                outFrameLeft + border + innerMargin + innerWidth, outFrameBottom - border - innerMargin);

        mBatteryPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(innerFrame, mBatteryPaint);

        /******绘制当前时间********/
        //底部的字显示的位置Y
        float y = mDisplayHeight - mTipPaint.getFontMetrics().bottom - tipMarginHeight;
        String time = StringUtils.dateConvert(System.currentTimeMillis(), "HH:mm");
        float x = outFrameLeft - mTipPaint.measureText(time) - ScreenUtils.dpToPx(mContext, 4);
        canvas.drawText(time, x, y, mTipPaint);
    }

    void drawContent(Bitmap bitmap) {
        Canvas canvas = new Canvas(bitmap);

        if (mPageMode == PageView.PAGE_MODE_SCROLL) {
            canvas.drawColor(mPageView.getPageBackground());
        }

        /******绘制内容****/
        if (mStatus != STATUS_FINISH) {
            //绘制字体
            String tip = "";
            switch (mStatus) {
                case STATUS_LOADING:
                    tip = "正在拼命加载中...";
                    break;
                case STATUS_ERROR:
                    tip = "加载失败(点击边缘重试)";
                    break;
                case STATUS_EMPTY:
                    tip = "文章内容为空";
                    break;
                case STATUS_PARSE:
                    tip = "正在排版请等待...";
                    break;
                case STATUS_PARSE_ERROR:
                    tip = "文件解析错误";
                    break;
            }

            //将提示语句放到正中间
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            float textHeight = fontMetrics.top - fontMetrics.bottom;
            float textWidth = mTextPaint.measureText(tip);
            float pivotX = (mDisplayWidth - textWidth) / 2;
            float pivotY = (mDisplayHeight - textHeight) / 2;
            canvas.drawText(tip, pivotX, pivotY, mTextPaint);
        } else {
            float top;
            if (mPageMode == PageView.PAGE_MODE_SCROLL) {
                top = -mTextPaint.getFontMetrics().top;
            } else {
                top = mMarginHeight - mTextPaint.getFontMetrics().top;
            }

            int interval = mIntervalSize + (int) mTextPaint.getTextSize();
            for (int i = 0; i < mCurPage.lines.size(); ++i) {
                String str = mCurPage.lines.get(i);
                canvas.drawText(str, mMarginWidth, top, mTextPaint);
                if (str.endsWith("\n")||str.endsWith("\r\n")) {
                    top += (interval + mParagraphSize);
                } else {
                    top += interval;
                }
            }
        }
    }

    void setDisplaySize(int w, int h) {
        //获取PageView的宽高
        mDisplayWidth = w;
        mDisplayHeight = h;
        //获取内容显示位置的大小
        mVisibleWidth = mDisplayWidth - mMarginWidth * 2;
        mVisibleHeight = mDisplayHeight - mMarginHeight * 2;

        //如果章节已显示，那么就重新计算页面
        if (mStatus == STATUS_FINISH) {
            //   mCurPageList = loadPageList(mCurChapterPos);
            int pageCount = mAdapter.getPageCount(mCurChapterPos, new PageProperty(mTextPaint, mVisibleWidth, mVisibleHeight, mIntervalSize, mParagraphSize));
            if (pageCount > 0) {
                List<TxtPage> txtPages = new ArrayList<>();
                for (int i = 0; i < pageCount; i++) {
                    txtPages.add(new TxtPage(i, mAdapter.getPageLines(mCurChapterPos, i, new PageProperty(mTextPaint, mVisibleWidth, mVisibleHeight, mIntervalSize, mParagraphSize))));
                }
                mCurPageList = txtPages;
            }
            //重新设置文章指针的位置
            mCurPage = getCurPage(mCurPage.position);
        }

        mPageView.drawCurPage(false);
    }

    //翻阅上一页
    boolean prev() {
        if (!checkStatus()) return false;

        //判断是否达到章节的起始点
        TxtPage prevPage = getPrevPage();
        if (prevPage == null) {
            //载入上一章。

            if (!prevChapter()) {
                return false;
            } else {
                mCancelPage = mCurPage;
                mCurPage = getPrevLastPage();
                mPageView.drawNextPage();
                return true;
            }
        }

        mCancelPage = mCurPage;
        mCurPage = prevPage;

        mPageView.drawNextPage();
        return true;
    }

    //加载上一章
    boolean prevChapter() {
        //判断是否上一章节为空
        if (!mAdapter.hasPreviousSection(mCurChapterPos)) {
            ToastUtils.showToast(mContext, "已经没有上一章了");
            return false;
        }

        //加载上一章数据
        int prevChapter = mCurChapterPos - 1;
        //当前章变成下一章
        mNextPageList = mCurPageList;

        //判断上一章缓存是否存在，如果存在则从缓存中获取数据。
        if (mWeakPrePageList != null && mWeakPrePageList.get() != null) {
            mCurPageList = mWeakPrePageList.get();
            mWeakPrePageList = null;
        }
        //如果不存在则加载数据
        else {
            int pageCount = mAdapter.getPageCount(prevChapter, new PageProperty(mTextPaint, mVisibleWidth, mVisibleHeight, mIntervalSize, mParagraphSize));
            if (pageCount > 0) {
                List<TxtPage> txtPages = new ArrayList<>();
                for (int i = 0; i < pageCount; i++) {
                    txtPages.add(new TxtPage(i, mAdapter.getPageLines(prevChapter, i, new PageProperty(mTextPaint, mVisibleWidth, mVisibleHeight, mIntervalSize, mParagraphSize))));
                }
                mCurPageList = txtPages;
            }
            // mCurPageList = loadPageList(prevChapter);
        }

        mLastChapter = mCurChapterPos;
        mCurChapterPos = prevChapter;

        if (mCurPageList != null) {
            mStatus = STATUS_FINISH;
        }
        //如果当前章不存在，则表示在加载中
        else {
            mStatus = STATUS_LOADING;
            //重置position的位置，防止正在加载的时候退出时候存储的位置为上一章的页码
            mCurPage.position = 0;
            mPageView.drawNextPage();
        }

        if (mPageChangeListener != null) {
            mPageChangeListener.onChapterChange(mCurChapterPos);
        }

        return true;
    }

    //翻阅下一页
    boolean next() {
        if (!checkStatus()) return false;
        //判断是否到最后一页了
        TxtPage nextPage = getNextPage();

        if (nextPage == null) {
            if (!nextChapter()) {
                return false;
            } else {
                mCancelPage = mCurPage;
                mCurPage = getCurPage(0);
                mPageView.drawNextPage();
                return true;
            }
        }

        mCancelPage = mCurPage;
        mCurPage = nextPage;
        mPageView.drawNextPage();
        return true;
    }

    boolean nextChapter() {
        //加载一章
        if (!mAdapter.hasNextSection(mCurChapterPos)) {
            ToastUtils.showToast(mContext, "已经没有下一章了");
            return false;
        }

        //如果存在下一章，则存储当前Page列表为上一章
        if (mCurPageList != null) {
            mWeakPrePageList = new WeakReference<List<TxtPage>>(new ArrayList<>(mCurPageList));
        }

        int nextChapter = mCurChapterPos + 1;
        //如果存在下一章预加载章节。
        if (mNextPageList != null) {
            mCurPageList = mNextPageList;
            mNextPageList = null;
        } else {
            //这个PageList可能为 null，可能会造成问题。
            int pageCount = mAdapter.getPageCount(nextChapter, new PageProperty(mTextPaint, mVisibleWidth, mVisibleHeight, mIntervalSize, mParagraphSize));
            if (pageCount > 0) {
                List<TxtPage> txtPages = new ArrayList<>();
                for (int i = 0; i < pageCount; i++) {
                    txtPages.add(new TxtPage(i, mAdapter.getPageLines(nextChapter, i, new PageProperty(mTextPaint, mVisibleWidth, mVisibleHeight, mIntervalSize, mParagraphSize))));
                }
                mCurPageList = txtPages;
            }
        }

        mLastChapter = mCurChapterPos;
        mCurChapterPos = nextChapter;

        //如果存在当前章，预加载下一章
        if (mCurPageList != null) {
            mStatus = STATUS_FINISH;
            preLoadNextChapter();
        }
        //如果当前章不存在，则表示在加载中
        else {
            mStatus = STATUS_LOADING;
            //重置position的位置，防止正在加载的时候退出时候存储的位置为上一章的页码
            mCurPage.position = 0;
            mPageView.drawNextPage();
        }

        if (mPageChangeListener != null) {
            mPageChangeListener.onChapterChange(mCurChapterPos);
        }
        return true;
    }

    //预加载下一章
    private void preLoadNextChapter() {
        //判断是否存在下一章
        if (mCurChapterPos + 1 >= mAdapter.getSectionCount()) {
            return;
        }
        //判断下一章的文件是否存在
        int nextChapter = mCurChapterPos + 1;

    }

    //取消翻页 (这个cancel有点歧义，指的是不需要看的页面)
    void pageCancel() {
        //加载到下一章取消了
        if (mCurPage.position == 0 && mCurChapterPos > mLastChapter) {
            prevChapter();
        }
        //加载上一章取消了 (可能有点小问题)
        else if (mCurPageList == null ||
                (mCurPage.position == mCurPageList.size() - 1 && mCurChapterPos < mLastChapter)) {
            nextChapter();
        }
        //假设加载到下一页了，又取消了。那么需要重新装载的问题
        mCurPage = mCancelPage;
    }

    /**
     * @return:获取初始显示的页面
     */
    TxtPage getCurPage(int pos) {
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageChange(pos);
        }
        return mCurPageList.get(pos);
    }

    /**
     * @return:获取上一个页面
     */
    private TxtPage getPrevPage() {
        int pos = mCurPage.position - 1;
        if (pos < 0) {
            return null;
        }
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageChange(pos);
        }
        return mCurPageList.get(pos);
    }

    /**
     * @return:获取下一的页面
     */
    private TxtPage getNextPage() {
        int pos = mCurPage.position + 1;
        if (pos >= mCurPageList.size()) {
            return null;
        }
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageChange(pos);
        }
        return mCurPageList.get(pos);
    }

    /**
     * @return:获取上一个章节的最后一页
     */
    private TxtPage getPrevLastPage() {
        int pos = mCurPageList.size() - 1;
        return mCurPageList.get(pos);
    }

    /**
     * 检测当前状态是否能够进行加载章节数据
     *
     * @return
     */
    private boolean checkStatus() {
        if (mStatus == STATUS_LOADING) {
            ToastUtils.showToast(mContext, "正在加载中，请稍等");
            return false;
        } else if (mStatus == STATUS_ERROR) {
            //点击重试
            mStatus = STATUS_LOADING;
            mPageView.drawCurPage(false);
            return false;
        }
        //由于解析失败，让其退出
        return true;
    }


    public void setTextColor(int color) {
        mTextPaint.setColor(color);
        mBatteryPaint.setColor(color);
        mTipPaint.setColor(color);
    }

    public int getPageMode() {
        return mPageMode;
    }


    public void setPageBackground(int color) {
        mPageView.setBgColor(color);
    }


}
