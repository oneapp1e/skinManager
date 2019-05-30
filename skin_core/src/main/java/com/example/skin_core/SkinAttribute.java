package com.example.skin_core;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.example.skin_core.utils.SkinResources;
import com.example.skin_core.utils.SkinThemeUtils;

import java.util.ArrayList;
import java.util.List;

public class SkinAttribute {

    private static final List<String> M_ATTRIBUTES = new ArrayList<>();


    static {
        M_ATTRIBUTES.add("background");
        M_ATTRIBUTES.add("scr");

        M_ATTRIBUTES.add("textColor");
        M_ATTRIBUTES.add("drawableLeft");
        M_ATTRIBUTES.add("drawableTop");
        M_ATTRIBUTES.add("drawableRight");
        M_ATTRIBUTES.add("drawableBottom");
        M_ATTRIBUTES.add("skinTypeface");
    }

    private Typeface typeface;

    public SkinAttribute(Typeface typeface) {
        this.typeface = typeface;
    }

    List<SkinView> skinViews = new ArrayList<>();

    public void load(Context context, View view, AttributeSet attributeSet) {
        List<SkinPair> skinPairs = new ArrayList<>();

        for (int i = 0; i < attributeSet.getAttributeCount(); i++) {
            String attributeName = attributeSet.getAttributeName(i);
            //是否符合筛选的属性名
            if (M_ATTRIBUTES.contains(attributeName)) {
                String attributeValue = attributeSet.getAttributeValue(i);
                if (attributeValue.startsWith("#")) {
                    continue;
                }

                int resId;
                if (attributeValue.startsWith("?")) {
                    int attId = Integer.parseInt(attributeValue.substring(1));
                    resId = SkinThemeUtils.getResId(view.getContext(), new int[]{attId})[0];
                } else {
                    //@1212112
                    resId = Integer.parseInt(attributeValue.substring(1));
                }

                if (resId != 0) {
                    //可以被替换的属性
                    SkinPair skinPair = new SkinPair(attributeName, resId);
                    skinPairs.add(skinPair);
                }
            }
        }
        //将view与之对应的可以动态替换的属性集合放入集合中   //没有属性满足 但是需要修改字体
        if (!skinPairs.isEmpty() || view instanceof TextView || view instanceof SkinViewSupport) {
            SkinView skinView = new SkinView(context, view, skinPairs);
            skinView.applySkin(typeface);
            skinViews.add(skinView);
        }
    }

    /**
     * 更新皮肤
     *
     * @param typeface 字体
     */
    public void applySkin(Typeface typeface) {
        for (SkinView skinView : skinViews) {
            skinView.applySkin(typeface);
        }
    }

    class SkinView {
        Context context;
        View view;
        List<SkinPair> skinPairs;

        private SkinView(Context context, View view, List<SkinPair> skinPairs) {
            this.context = context;
            this.view = view;
            this.skinPairs = skinPairs;
        }

        private void applySkin(Typeface typeface) {
            applySkinTypeface(typeface);
            applySkinViewSupport();
            //
            for (SkinPair skinPair : skinPairs) {
                Drawable left = null, top = null, right = null, bottom = null;
                switch (skinPair.attributeName) {
                    case "background":
                        Object background = SkinResources.getInstance(context).getBackground(skinPair.resId);
                        if (background instanceof Integer) {
                            view.setBackgroundColor((Integer) background);
                        } else {
                            ViewCompat.setBackground(view, (Drawable) background);
                        }
                        break;
                    case "src":
                        background = SkinResources.getInstance(context).getBackground(skinPair
                                .resId);
                        if (background instanceof Integer) {
                            ((ImageView) view).setImageDrawable(new ColorDrawable((Integer)
                                    background));
                        } else {
                            ((ImageView) view).setImageDrawable((Drawable) background);
                        }
                        break;
                    case "textColor":
                        ((TextView) view).setTextColor(SkinResources.getInstance(context).getColorStateList
                                (skinPair.resId));
                        break;
                    case "drawableLeft":
                        left = SkinResources.getInstance(context).getDrawable(skinPair.resId);
                        break;
                    case "drawableTop":
                        top = SkinResources.getInstance(context).getDrawable(skinPair.resId);
                        break;
                    case "drawableRight":
                        right = SkinResources.getInstance(context).getDrawable(skinPair.resId);
                        break;
                    case "drawableBottom":
                        bottom = SkinResources.getInstance(context).getDrawable(skinPair.resId);
                        break;
                    case "skinTypeface":
                        Typeface typeface1 = SkinResources.getInstance(context).getTypeface(skinPair.resId);
                        applySkinTypeface(typeface1);
                        break;
                    default:
                        break;
                }
                if (null != left || null != right || null != top || null != bottom) {
                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(left, top, right,
                            bottom);
                }
            }
        }

        private void applySkinTypeface(Typeface typeface) {
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(typeface);
            }
        }


        private void applySkinViewSupport() {
            if (view instanceof SkinViewSupport) {
                ((SkinViewSupport) view).applySkin();
            }
        }
    }

    class SkinPair {
        String attributeName;
        int resId;

        public SkinPair(String attributeName, int resId) {
            this.attributeName = attributeName;
            this.resId = resId;
        }
    }
}
