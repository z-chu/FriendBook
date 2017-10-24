package com.youshibi.app.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.youshibi.app.BuildConfig;
import com.youshibi.app.R;
import com.youshibi.app.base.BaseActivity;
import com.youshibi.app.ui.help.ToolbarHelper;
import com.youshibi.app.util.Shares;

/**
 * author : zchu
 * date   : 2017/10/24
 * desc   :
 */

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = ToolbarHelper.initToolbar(this, R.id.toolbar, true, "关于友书");
        TextView textView=findViewById(R.id.tv_version_name);
        textView.setText(getString(R.string.app_name)+" "+BuildConfig.VERSION_NAME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_about, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Shares.share(this, R.string.share_text);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
