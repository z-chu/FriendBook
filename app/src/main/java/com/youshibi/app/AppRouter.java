package com.youshibi.app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.youshibi.app.data.bean.Book;
import com.youshibi.app.data.db.table.BookTb;
import com.youshibi.app.pref.AppConfig;
import com.youshibi.app.presentation.AboutActivity;
import com.youshibi.app.presentation.CrashActivity;
import com.youshibi.app.presentation.book.BookCatalogActivity;
import com.youshibi.app.presentation.book.BookDetailActivity;
import com.youshibi.app.presentation.main.MainActivity;
import com.youshibi.app.presentation.read.ReadActivity;
import com.youshibi.app.presentation.search.SearchActivity;
import com.youshibi.app.presentation.search.SearchResultActivity;
import com.youshibi.app.util.CountEventHelper;
import com.youshibi.app.util.DensityUtil;
import com.youshibi.app.util.ToastUtil;
import com.zchu.log.Logger;


/**
 * 作者: 赵成柱 on 2016/7/13.
 * app路由，界面跳转帮助类，所有的界面跳转通过此类进行跳转,包括组件交互
 */
public class AppRouter {


    /**
     * 获取全局加载dialog
     */
    public static Dialog getLoadingDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.AlertDialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        dialog.setContentView(view, new ViewGroup.LayoutParams(DensityUtil.dp2px(context, 96), DensityUtil.dp2px(context, 96)));
        return dialog;
    }


    public static void showAppMarket(Context context) {
        try {
            String mAddress = "market://details?id=" + context.getPackageName();
            Intent marketIntent = new Intent("android.intent.action.VIEW");
            marketIntent.setData(Uri.parse(mAddress));
            context.startActivity(marketIntent);
        } catch (Exception e) {
            Logger.e(e);
            ToastUtil.showToast("亲，您未安装任何应用商店，无法评论。");
        }
    }

    /**
     * 跳转到当前应用设置界面
     */
    public static void showAppDetailSetting(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }


    /**
     * @param webView
     */
    @SuppressLint("SetJavaScriptEnabled")
    public static void initWebViewSettings(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDefaultFontSize(15);
        //设置缓存
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);

        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCachePath(AppConfig.WEB_CACHE_PATH);
    }

    public static void showMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示崩溃提示
     */
    public static void showCrashActivity(Context context, String message, final String errorInfo) {
        context.startActivity(CrashActivity.newIntent(context, message, errorInfo));
    }

    public static void showBookDetailActivity(Context context, Book book) {
        context.startActivity(BookDetailActivity.newIntent(context, book));
        CountEventHelper.countBookDetail(context, book);
    }

    public static void showReadActivity(Context context, Book book, Integer sectionIndex, String sectionId) {
        context.startActivity(ReadActivity.newIntent(context, book, sectionIndex, sectionId));
        CountEventHelper.countBookRead(context, book.getId(), book.getName());
    }

    public static void showReadActivity(Context context, BookTb bookTb) {
        context.startActivity(ReadActivity.newIntent(context, bookTb));
        CountEventHelper.countBookRead(context, bookTb.getId(), bookTb.getName());
    }

    public static void showSearchActivity(Context context) {
        showSearchActivity(context, null);
    }

    public static void showSearchActivity(Context context, String keyword) {
        context.startActivity(SearchActivity.newIntent(context, keyword));

    }

    public static void showSearchResultActivity(Context context, String keyword) {
        context.startActivity(SearchResultActivity.newIntent(context, keyword));
        CountEventHelper.countBookSearch(context, keyword);
    }

    public static void showBookCatalogActivity(Context context, Book book, int sectionCount) {
        context.startActivity(BookCatalogActivity.newIntent(context, book, sectionCount));
    }

    public static void showAboutActivity(Context context) {
        context.startActivity(new Intent(context, AboutActivity.class));
    }

}
