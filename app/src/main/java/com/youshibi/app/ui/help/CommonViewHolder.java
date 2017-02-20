package com.youshibi.app.ui.help;

import android.net.Uri;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.File;

/**
 * Created by zchu on 16-12-1.
 * 如有其他定制化的封装可以自行添加
 */

public class CommonViewHolder extends BaseViewHolder{
    public CommonViewHolder(View view) {
        super(view);
    }

    public CommonViewHolder loadImage(@IdRes int viewId, String string){
        Glide
                .with(convertView.getContext())
                .load(string)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into((ImageView) getView(viewId));
        return  this;
    }

    public CommonViewHolder loadImage(@IdRes int viewId, Uri uri){
        Glide
                .with(convertView.getContext())
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into((ImageView) getView(viewId));
        return  this;
    }

    public CommonViewHolder loadImage(@IdRes int viewId, File file){
        Glide
                .with(convertView.getContext())
                .load(file)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into((ImageView) getView(viewId));
        return  this;
    }

    public CommonViewHolder loadImage(@IdRes int viewId, byte[] model){
        Glide
                .with(convertView.getContext())
                .load(model)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into((ImageView) getView(viewId));
        return  this;
    }


}
