package com.example.skin_core;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.example.skin_core.utils.SkinThemeUtils;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class SkinLayoutFactory implements LayoutInflater.Factory2, Observer {

    private static final String[] mClassPrefixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };

    static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};

    private static final HashMap<String, Constructor<? extends View>> sConstructorMap =
            new HashMap<>();

    /**
     * 属性处理类
     */
    private SkinAttribute skinAttribute;
    private Activity activity;

    public SkinLayoutFactory(Activity activity, Typeface typeface) {
        this.activity = activity;
        skinAttribute = new SkinAttribute(typeface);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attributeSet) {
        //反射
        View view = createViewFromTag(name, context, attributeSet);
        if (view == null) {
            //自定义view
            view = CreateView(name, context, attributeSet);
        }
        //筛选符合属性的类
        skinAttribute.load(context, view, attributeSet);

        return view;
    }

    private View createViewFromTag(String name, Context context, AttributeSet attributeSet) {
        if (-1 != name.indexOf(".")) {
            return null;
        }
        View view;
        for (int i = 0; i < mClassPrefixList.length; i++) {
            view = CreateView(mClassPrefixList[i] + name, context, attributeSet);
            if (view != null) {
                return view;
            }
        }
        return null;
    }

    private View CreateView(String name, Context context, AttributeSet attributeSet) {
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        if (constructor == null) {
            try {
                Class<? extends View> aClass = context.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = aClass.getConstructor(mConstructorSignature);
                sConstructorMap.put(name, constructor);
            } catch (Exception e) {

            }
        }

        if (constructor != null) {
            try {
                return constructor.newInstance(context, attributeSet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attributeSet) {
        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        //更换皮肤
        Typeface typeface = SkinThemeUtils.getSkinTypeface(activity);
        //更新状态栏颜色
        SkinThemeUtils.updateStatusBarColor(activity);
        skinAttribute.applySkin(typeface);
    }

}
