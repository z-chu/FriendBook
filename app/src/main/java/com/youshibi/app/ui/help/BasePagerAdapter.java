package com.youshibi.app.ui.help;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 啟成 on 2015/12/14.
 */
public abstract class BasePagerAdapter<T> extends PagerAdapter {


    protected List<T> data;
    protected List<String> strPageTitles;
    protected List<Integer> layoutResIds;


    public BasePagerAdapter(List<T> data, int layoutResId) {
        this.data = data != null ? data : new ArrayList<T>();
        layoutResIds=new ArrayList<Integer>();
        for (int i = 0; i < data.size(); i++) {
            layoutResIds.add(layoutResId);
        }
    }
    public BasePagerAdapter(List<T> data, List<Integer> layoutResIds) {
       this(data,layoutResIds,null);
    }

    public BasePagerAdapter(List<T> data, List<Integer> layoutResIds, List<String> strPageTitles) {
        this.data = data == null ? new ArrayList<T>() : data;
        this.layoutResIds = layoutResIds;
        this.strPageTitles = strPageTitles;
    }




    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(layoutResIds.get(position % layoutResIds.size()), container, false);

        convert(view, getItem(position),position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public String getPageTitle(int position) {
        return strPageTitles==null?null:strPageTitles.get(position);
    }


    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public T getItem(int position) {
        if (data==null||position >= data.size()) return null;
        return data.get(position);
    }


    protected abstract void convert(View view, T item, int position);


    public void addAll(List<T> data) {
        this.data = new ArrayList<>(data);
        notifyDataSetChanged();
    }
}
