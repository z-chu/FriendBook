package com.youshibi.app.ui.help;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by Chu on 2016/11/20.
 * <p>
 * Toolbar委托,帮你初始化好Toolbar
 */

public class ToolbarHelper {

    public static Toolbar initToolbar(@NonNull final AppCompatActivity activity, @IdRes int toolbarId, boolean canBack, @Nullable CharSequence title) {
        Toolbar toolbar = (Toolbar) activity.findViewById(toolbarId);
        if (toolbar == null) {
            throw new IllegalStateException(
                    "The subclass of ToolbarActivity must contain a toolbar.");
        }
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            if (canBack) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                if (title != null) {
                    actionBar.setTitle(title);
                } else {
                    actionBar.setDisplayShowTitleEnabled(false);
                }
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.onBackPressed();
                    }
                });
            }

        }
        return toolbar;
    }

}
