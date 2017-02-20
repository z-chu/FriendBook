package com.youshibi.app.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by z-chu on 2016/4/8.
 */
public class DensityUtil {
    private DensityUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    /** dp转px */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /** sp转px */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /** px转dp */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /** px转sp */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

}
