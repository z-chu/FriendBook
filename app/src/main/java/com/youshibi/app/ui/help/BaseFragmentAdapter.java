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
	private List<Fragment> FragmentPages;
	private List<String> PageTitles;
	private FragmentManager fragmentManager;

	public BaseFragmentAdapter(FragmentManager fm) {
		super(fm);
		this.fragmentManager = fm;
	}

	
	@Override
	public Fragment getItem(int position) {
		return FragmentPages.get(position);
	}
	@Override
	public int getCount() {
		return FragmentPages != null ? FragmentPages.size() : 0;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	public void setFragmentPages(List<Fragment> FragmentPages) {
		if (this.FragmentPages != null) {
			FragmentTransaction ft = fragmentManager.beginTransaction();
			for (Fragment f : this.FragmentPages) {
				ft.remove(f);
			}
			ft.commit();
			ft = null;
			fragmentManager.executePendingTransactions();
		}
		this.FragmentPages = FragmentPages;
		notifyDataSetChanged();
	}

	/**
	 * 设置每页的标题
	 * 
	 * @param PageTitles
	 */
	public void setPageTitleList(List<String> PageTitles) {
		if (this.PageTitles != null) {
			this.PageTitles.clear();
			for (String title : PageTitles) {
				this.PageTitles.add(title);
			}
		} else {
			this.PageTitles = PageTitles;
		}
	}

	public void setPageTitleArray(String[] PageTitles) {
		if (this.PageTitles != null) {
			this.PageTitles.clear();
		}else{
			this.PageTitles=new ArrayList<String>();
		}
		for (int i = 0; i < PageTitles.length; i++) {
			this.PageTitles.add(PageTitles[i]);
		}
	}

	public List<String> getPageTitles() {
		return PageTitles;
	}

	public List<Fragment> getFragmentPages() {
		return FragmentPages;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (PageTitles != null && position < PageTitles.size()) {
			return PageTitles.get(position);
		}
		return super.getPageTitle(position);
	}

}
