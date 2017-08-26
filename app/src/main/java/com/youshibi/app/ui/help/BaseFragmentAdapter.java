package com.youshibi.app.ui.help;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author z.chu 添加notifyDataSetChanged刷新FragmentPage功能,请通过setFragmentPages设置页数据
 */
public class BaseFragmentAdapter extends FragmentPagerAdapter {
	private Fragment[] FragmentPages;
	private String[] PageTitles;
	private FragmentManager fragmentManager;

	public BaseFragmentAdapter(FragmentManager fm) {
		super(fm);
		this.fragmentManager = fm;
	}

	
	@Override
	public Fragment getItem(int position) {
		return FragmentPages[position];
	}
	@Override
	public int getCount() {
		return FragmentPages != null ? FragmentPages.length: 0;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	public void setFragmentPages(List<Fragment> FragmentPages) {
		setFragmentPages((Fragment[]) FragmentPages.toArray());
	}

	public void setFragmentPages(Fragment[] FragmentPages) {
		if (this.FragmentPages != null) {
			FragmentTransaction ft = fragmentManager.beginTransaction();
			for (Fragment f : this.FragmentPages) {
				ft.remove(f);
			}
			ft.commitAllowingStateLoss();
			fragmentManager.executePendingTransactions();
		}
		this.FragmentPages = FragmentPages;
		notifyDataSetChanged();
	}

	public void setPageTitles(ArrayList<String> PageTitles) {
		setPageTitles((String[]) PageTitles.toArray()) ;
	}

	public void setPageTitles(String[] PageTitles) {
		this.PageTitles = PageTitles;

	}

	public String[] getPageTitles() {
		return PageTitles;
	}

	public Fragment[] getFragmentPages() {
		return FragmentPages;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (PageTitles != null && position < PageTitles.length) {
			return PageTitles[position];
		}
		return super.getPageTitle(position);
	}

}
