package com.example.skin_core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import com.example.skin_core.utils.SkinPreference;
import com.example.skin_core.utils.SkinResources;

import java.lang.reflect.Method;
import java.util.Observable;

public class SkinManager extends Observable {

    private static volatile SkinManager instance;
    private SkinActivityLifecycle skinActivityLifecycle;

    private SkinManager(Application application) {
        skinActivityLifecycle = new SkinActivityLifecycle();
        application.registerActivityLifecycleCallbacks(skinActivityLifecycle);
        //load
        String path = SkinPreference.getInstance(application.getApplicationContext()).getSkin();
        loadSkin(application.getApplicationContext(), path);
    }

    public static void init(Application application) {
        if (instance == null) {
            synchronized (SkinManager.class) {
                if (instance == null) {
                    instance = new SkinManager(application);
                }
            }
        }
    }

    public static SkinManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("没有调用 init方法");
        }
        return instance;
    }

    /**
     * 加载皮肤包 并更新
     */
    public void loadSkin(Context context, String path) {
        //如果为空 还原默认皮肤包
        if (TextUtils.isEmpty(path)) {
            SkinPreference.getInstance(context).setSkin("");
            SkinResources.getInstance(context).reset();
        } else {
            try {
                AssetManager assetManager = AssetManager.class.newInstance();
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                addAssetPath.setAccessible(true);
                addAssetPath.invoke(assetManager, path);

                Resources resources = context.getResources();
                Resources skinResources = new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());

                PackageManager packageManager = context.getApplicationContext().getPackageManager();
                PackageInfo info = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);

                SkinResources.getInstance(context).applySkin(skinResources, info.packageName);
                //保存当前皮肤包
                SkinPreference.getInstance(context).setSkin(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //应用皮肤包
        setChanged();
        //通知观察者
        notifyObservers();
    }

    /**
     * 为了应对tablayout这种特殊view  应用重新打开字体不起作用
     */
    public void applyUpdate(Activity activity) {
        skinActivityLifecycle.applyUpdate(activity);
    }
}
