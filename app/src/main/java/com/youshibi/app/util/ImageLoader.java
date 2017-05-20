package com.youshibi.app.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Chu on 2016/11/16.
 * 图片加载类,统一适配(方便换库,方便管理)
 */

public class ImageLoader {


    /**
     * @param String @param string A file path, or a uri or url
     */
    public static void load(Context context, String String, ImageView view) {
        Glide
                .with(context)
                .load(String)
                .into(view);
    }



    public static void clear(Context context) {

        Glide.getPhotoCacheDir(context).delete();
    }
}
