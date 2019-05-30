package com.example.skin_core;

import android.app.Activity;
import android.app.Application;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.core.view.LayoutInflaterCompat;

import com.example.skin_core.utils.SkinThemeUtils;

import java.lang.reflect.Field;
import java.util.HashMap;

public class SkinActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    HashMap<Activity, SkinLayoutFactory> mLayoutFactoryMap = new HashMap<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        //更新状态栏
        SkinThemeUtils.updateStatusBarColor(activity);
        Typeface skinTypeface = SkinThemeUtils.getSkinTypeface(activity);
        //更新属性
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        //利用反射那是属性值为false
        try {
            Field mFactorySetField = LayoutInflater.class.getDeclaredField("mFactorySet");
            mFactorySetField.setAccessible(true);
            mFactorySetField.setBoolean(layoutInflater, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SkinLayoutFactory skinLayoutFactory = new SkinLayoutFactory(activity, skinTypeface);
        LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutFactory);
        //注册观察者
        mLayoutFactoryMap.put(activity, skinLayoutFactory);
        SkinManager.getInstance().addObserver(skinLayoutFactory);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //删除观察者
        SkinLayoutFactory skinLayoutFactory = mLayoutFactoryMap.remove(activity);
        SkinManager.getInstance().deleteObserver(skinLayoutFactory);
    }

    public void applyUpdate(Activity activity) {
        SkinLayoutFactory skinLayoutFactory = mLayoutFactoryMap.get(activity);
        skinLayoutFactory.update(null, null);
    }
}
