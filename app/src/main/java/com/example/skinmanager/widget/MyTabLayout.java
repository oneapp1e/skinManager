package com.example.skinmanager.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.example.skin_core.SkinViewSupport;
import com.example.skin_core.utils.SkinResources;
import com.example.skinmanager.R;
import com.google.android.material.tabs.TabLayout;


public class MyTabLayout extends TabLayout implements SkinViewSupport {
    int tabIndicatorColorResId;
    int tabTextColorResId;

    public MyTabLayout(Context context) {
        this(context, null, 0);
    }

    public MyTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabLayout,
                defStyleAttr, 0);
        tabIndicatorColorResId = a.getResourceId(R.styleable.TabLayout_tabIndicatorColor, 0);
        tabTextColorResId = a.getResourceId(R.styleable.TabLayout_tabTextColor, 0);
        a.recycle();
    }

    @Override
    public void applySkin() {
        if (tabIndicatorColorResId != 0) {
            int tabIndicatorColor = SkinResources.getInstance(getContext()).getColor(tabIndicatorColorResId);
            setSelectedTabIndicatorColor(tabIndicatorColor);
        }

        if (tabTextColorResId != 0) {
            ColorStateList tabTextColor = SkinResources.getInstance(getContext()).getColorStateList
                    (tabTextColorResId);
            setTabTextColors(tabTextColor);
        }
    }
}
