package com.youshibi.app.ui.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * 作者：z-chu
 * 时间：2016/6/11 15:37
 * 邮箱：zchu8073@gmail.com
 */
public class InContentAnim {

    //单位dp
    private static final int ANIM_TRANSLATE_Y = 40;
    //动画持续时间
    private static final int ANIM_TIME_LONG = 500;
    private View contentView;
    private View loadingView;
    private AnimatorSet set;

    public InContentAnim(@NonNull View contentView, @NonNull View loadingView) {
        this.contentView = contentView;
        this.loadingView = loadingView;
    }


    public void start() {
        if (contentView.getVisibility() != View.VISIBLE) {
            if (set == null) {
                set = new AnimatorSet();
                ObjectAnimator contentFadeIn = ObjectAnimator.ofFloat(contentView, View.ALPHA, 0f, 1f);
                ObjectAnimator contentTranslateIn = ObjectAnimator.ofFloat(contentView, View.TRANSLATION_Y,
                        ANIM_TRANSLATE_Y, 0);
                ObjectAnimator loadingFadeOut = ObjectAnimator.ofFloat(loadingView, View.ALPHA, 1f, 0f);
                ObjectAnimator loadingTranslateOut = ObjectAnimator.ofFloat(loadingView, View.TRANSLATION_Y, 0,
                        -ANIM_TRANSLATE_Y);
                set.playTogether(contentFadeIn, contentTranslateIn, loadingFadeOut, loadingTranslateOut);

                set.setDuration(ANIM_TIME_LONG);
                set.addListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                        contentView.setTranslationY(0);
                        contentView.setVisibility(View.VISIBLE);
                        loadingView.setTranslationY(0);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        contentView.setTranslationY(0);
                        loadingView.setTranslationY(0);
                        loadingView.setVisibility(View.GONE);
                        loadingView.setAlpha(1f); // For future showLoading calls
                    }
                });

            }else{
                loadingView.setVisibility(View.GONE);
            }
            set.start();
        }
    }
}
