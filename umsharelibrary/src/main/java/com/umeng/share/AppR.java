package com.umeng.share;

import android.content.Context;
import android.text.TextUtils;

/**
 * 获取应用内资源的id
 */
public class AppR {

    public static int getResId(Context context, String resType, String resName) {
        int var3 = 0;
        if (context != null && !TextUtils.isEmpty(resType) && !TextUtils.isEmpty(resName)) {
            String var4 = context.getPackageName();
            if (TextUtils.isEmpty(var4)) {
                return var3;
            } else {
                var3 = context.getResources().getIdentifier(resName, resType, var4);
                if (var3 <= 0) {
                    var3 = context.getResources().getIdentifier(resName.toLowerCase(), resType, var4);
                }
            }
        }
        return var3;
    }

    public static int getBitmapRes(Context context, String resName) {
        return getResId(context, "drawable", resName);
    }

    public static int getStringRes(Context context, String resName) {
        return getResId(context, "string", resName);
    }

    public static int getStringArrayRes(Context context, String resName) {
        return getResId(context, "array", resName);
    }

    public static int getLayoutRes(Context context, String resName) {
        return getResId(context, "layout", resName);
    }

    public static int getStyleRes(Context context, String resName) {
        return getResId(context, "style", resName);
    }

    public static int getIdRes(Context context, String resName) {
        return getResId(context, "id", resName);
    }

    public static int getColorRes(Context context, String resName) {
        return getResId(context, "color", resName);
    }

    public static int getRawRes(Context context, String resName) {
        return getResId(context, "raw", resName);
    }

    public static int getPluralsRes(Context context, String resName) {
        return getResId(context, "plurals", resName);
    }

    public static int getAnimRes(Context context, String resName) {
        return getResId(context, "anim", resName);
    }
}
