package com.youshibi.app.presentation.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.youshibi.app.R;
import com.youshibi.app.mvp.MvpActivity;

/**
 * Created by Chu on 2016/11/30.
 */

public class MainActivity extends MvpActivity<MainPresenter> implements MainView {


   // private DrawerLayout drawer;
  //  private FrameLayout content;
    private ViewPager viewPager;
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private TabLayout tab;
    //private NavigationView navigation;

    private void findView() {
        //drawer = (DrawerLayout) findViewById(drawer);
       // content = (FrameLayout) findViewById(R.id.content);
       // LayoutInflater.from(this).inflate(R.layout.layout_main_content, content, true);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tab = (TabLayout) findViewById(R.id.tab);
        //navigation = (NavigationView) findViewById(R.id.navigation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_content);
        //systemBarTintManager.setStatusBarTintColor(Color.parseColor("#20000000"));
        findView();
       // setSupportActionBar(toolbar);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DisplayUtil.hasVirtualNavigationBar()) {
            content.setPadding(0, 0, 0, -DisplayUtil.getNavigationBarHeight());
        }*/
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.inflateMenu(R.menu.main);
       /* viewPager.setOffscreenPageLimit(2);
        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.no, R.string.off);
        mActionBarDrawerToggle.syncState();
        drawer.setDrawerListener(mActionBarDrawerToggle);*/
        getPresenter().initViewPage(getSupportFragmentManager());
    }


  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }*/



    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void setViewPage(PagerAdapter adapter) {
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());
        tab.setupWithViewPager(viewPager);

    }
}
