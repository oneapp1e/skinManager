package com.example.skin_core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;

import com.example.skin_core.R;

public class SkinThemeUtils {

    private static int[] APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = {
            androidx.appcompat.R.attr.colorPrimaryDark};

    private static int[] STATUSBAR_COLOR_ATTRS = {android.R.attr.statusBarColor, android.R.attr.navigationBarColor};

    private static int[] TYPEFACE_ATTRS = {R.attr.skinTypeface};

    public static int[] getResId(Context context, int[] attrs) {
        int[] resId = new int[attrs.length];
        TypedArray typedArray = context.obtainStyledAttributes(attrs);

        for (int i = 0; i < attrs.length; i++) {
            resId[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
        return resId;
    }

    /**
     * 更改StatusBarColor及NavigationBarColor
     */
    public static void updateStatusBarColor(Activity activity) {
        //5.0以上才能修改
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        int[] statusBarColorResId = getResId(activity, STATUSBAR_COLOR_ATTRS);
        if (statusBarColorResId[0] != 0) {
            activity.getWindow().setStatusBarColor(SkinResources.getInstance(activity).getColor(statusBarColorResId[0]));
        } else {
            int[] colorPrimaryDarkResId = getResId(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS);
            if (colorPrimaryDarkResId[0] != 0) {
                activity.getWindow().setStatusBarColor(SkinResources.getInstance(activity).getColor(colorPrimaryDarkResId[0]));
            }
        }

        if (statusBarColorResId[1] != 0) {
            activity.getWindow().setNavigationBarColor(SkinResources.getInstance(activity).getColor(statusBarColorResId[1]));
        }
    }

    /**
     * 获取字体
     */
    public static Typeface getSkinTypeface(Context context) {
        int[] typefaceId = getResId(context, TYPEFACE_ATTRS);
        return SkinResources.getInstance(context).getTypeface(typefaceId[0]);
    }

}
