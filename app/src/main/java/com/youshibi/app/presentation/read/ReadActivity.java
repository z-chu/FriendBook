package com.youshibi.app.presentation.read;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.gyf.barlibrary.ImmersionBar;
import com.youshibi.app.R;
import com.youshibi.app.data.bean.BookSectionContent;
import com.youshibi.app.mvp.MvpActivity;
import com.youshibi.app.ui.help.ToolbarHelper;
import com.youshibi.app.util.DisplayUtil;
import com.youshibi.app.util.SystemBarUtils;
import com.zchu.reader.PageLoaderAdapter;
import com.zchu.reader.PageView;
import com.zchu.reader.ReadSettingManager;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Chu on 2017/5/28.
 */

public class ReadActivity extends MvpActivity<ReadContract.Presenter> implements ReadContract.View {

    private static final String K_EXTRA_BOOK_ID = "book_id";
    private static final String K_EXTRA_BOOK_NAME = "book_name";
    private static final String K_EXTRA_SECTION_INDEX = "section_index";


    private PageView readView;
    private AppBarLayout appBar;
    private View readBottom;

    private Animation mTopInAnim;
    private Animation mTopOutAnim;
    private Animation mBottomInAnim;
    private Animation mBottomOutAnim;
    private boolean canTouch = true;

    private BookSectionContent mData;

    private boolean isFullScreen = false;

    public static Intent newIntent(Context context, String bookId, int sectionIndex) {
        Intent intent = new Intent(context, ReadActivity.class);
        intent
                .putExtra(K_EXTRA_BOOK_ID, bookId)
                .putExtra(K_EXTRA_SECTION_INDEX, sectionIndex);
        return intent;
    }

    public static Intent newIntent(Context context, String bookId, String bookName, int sectionIndex) {
        Intent intent = new Intent(context, ReadActivity.class);
        intent
                .putExtra(K_EXTRA_BOOK_ID, bookId)
                .putExtra(K_EXTRA_BOOK_NAME, bookName)
                .putExtra(K_EXTRA_SECTION_INDEX, sectionIndex);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        String bookName = getIntent().getStringExtra(K_EXTRA_BOOK_NAME);
        ToolbarHelper.initToolbar(this, R.id.toolbar, true,
                bookName == null ? getString(R.string.app_name) : bookName);
        ReadSettingManager.init(this);
        readView = (PageView) findViewById(R.id.pv_read);
        appBar = (AppBarLayout) findViewById(R.id.appbar);
        readBottom = findViewById(R.id.read_bottom);

        if (Build.VERSION.SDK_INT >= 19) {
            appBar.setPadding(0, DisplayUtil.getStateBarHeight(this), 0, 0);
        }
        //半透明化StatusBar
        SystemBarUtils.transparentStatusBar(this);
        //隐藏StatusBar
        appBar.post(new Runnable() {
            @Override
            public void run() {
                hideSystemBar();
            }
        });


        readView.setPageMode(PageView.PAGE_MODE_COVER);
        readView.setTouchListener(new PageView.TouchListener() {
            @Override
            public void center() {
                toggleMenu(true);
            }

            @Override
            public void cancel() {

            }
        });
        readView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() != MotionEvent.ACTION_UP) {
                    canTouch = hideReadMenu();
                    return canTouch;
                } else {
                    if (canTouch) {
                        canTouch = true;
                        return true;
                    }
                }
                return false;
            }
        });
        getPresenter().start();
        getPresenter().loadData();

    }

    @Override
    protected void initImmersionBar(ImmersionBar immersionBar) {
        immersionBar.init();
    }

    @Override
    protected boolean isEnableSlideFinish() {
        return false;
    }

    @Override
    public void showContent() {
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void showError(String errorMsg) {
    }

    @NonNull
    @Override
    public ReadContract.Presenter createPresenter() {
        return new ReadPresenter(
                getIntent().getStringExtra(K_EXTRA_BOOK_ID),
                getIntent().getIntExtra(K_EXTRA_SECTION_INDEX, 0)
        );
    }



    @Override
    public void setPageAdapter(PageLoaderAdapter adapter) {
        readView.setAdapter(adapter);
        readView.setOnPageChangeListener(getPresenter());
    }

    @Override
    public void openSection(int section) {
        readView.openSection(section);
    }

    /**
     * 切换菜单栏的可视状态
     * 默认是隐藏的
     */
    private void toggleMenu(boolean hideStatusBar) {
        initMenuAnim();

        if (appBar.getVisibility() == VISIBLE) {
            //关闭
            appBar.startAnimation(mTopOutAnim);
            readBottom.startAnimation(mBottomOutAnim);
            appBar.setVisibility(GONE);
            readBottom.setVisibility(GONE);

            if (hideStatusBar) {
                hideSystemBar();
            }
        } else {
            appBar.setVisibility(VISIBLE);
            readBottom.setVisibility(VISIBLE);
            appBar.startAnimation(mTopInAnim);
            readBottom.startAnimation(mBottomInAnim);

            showSystemBar();
        }
    }

    /**
     * 隐藏阅读界面的菜单显示
     *
     * @return 是否隐藏成功
     */
    private boolean hideReadMenu() {
        hideSystemBar();
        if (appBar.getVisibility() == VISIBLE) {
            toggleMenu(true);
            return true;
        }
        return false;
    }


    //初始化菜单动画
    private void initMenuAnim() {
        if (mTopInAnim != null) return;

        mTopInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_in);
        mTopOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_out);
        mBottomInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_in);
        mBottomOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_out);
        //退出的速度要快
        mTopOutAnim.setDuration(200);
        mBottomOutAnim.setDuration(200);
    }


    private void showSystemBar() {
        //显示
        SystemBarUtils.showUnStableStatusBar(this);
        if (isFullScreen) {
            SystemBarUtils.showUnStableNavBar(this);
        }
    }

    private void hideSystemBar() {
        //隐藏
        SystemBarUtils.hideStableStatusBar(this);
        if (isFullScreen) {
            SystemBarUtils.hideStableNavBar(this);
        }
    }
}
