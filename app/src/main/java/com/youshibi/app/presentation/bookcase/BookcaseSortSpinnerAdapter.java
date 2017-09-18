package com.youshibi.app.presentation.bookcase;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.youshibi.app.R;
import com.youshibi.app.util.DensityUtil;

/**
 * author : zchu
 * date   : 2017/9/14
 * desc   :
 */

public class BookcaseSortSpinnerAdapter extends BaseAdapter {
    private Context mContext;
    private String[] strings;
    private int selectedPosition =-1;

    public BookcaseSortSpinnerAdapter(Context context, String[] strings) {
        mContext = context;
        this.strings = strings;
    }

    @Override
    public int getCount() {
        return strings.length;
    }

    @Override
    public String getItem(int position) {
        return strings[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            textView = new TextView(mContext);
            textView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(mContext, 40)));
            textView.setPadding(DensityUtil.dp2px(mContext, 8), 0, DensityUtil.dp2px(mContext, 40), 0);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            textView.setTextSize(14);
            TextPaint paint = textView.getPaint();
            paint.setFakeBoldText(true);

        } else {
            textView = (TextView) convertView;
        }
        textView.setText(getItem(position));
        if(selectedPosition >=0&&position== selectedPosition){
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        }else{
            textView.setTextColor(ContextCompat.getColor(mContext,R.color.textPrimary));
        }

        return textView;
    }

    public void setSelectedPosition(int position){
        selectedPosition =position;

    }
}
