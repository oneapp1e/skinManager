package com.example.skinmanager;

import android.app.Application;

import com.example.skin_core.SkinManager;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
