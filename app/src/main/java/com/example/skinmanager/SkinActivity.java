package com.example.skinmanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.example.skin_core.SkinManager;


public class SkinActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);
    }

    public void change(View view) {
        PermissionUtils.permission(PermissionConstants.STORAGE).callback(new PermissionUtils.SimpleCallback() {
            @Override
            public void onGranted() {
                //换肤
                SkinManager.getInstance().loadSkin(SkinActivity.this, "/sdcard/app-skin-debug.skin");
            }

            @Override
            public void onDenied() {

            }
        }).rationale(new PermissionUtils.OnRationaleListener() {
            @Override
            public void rationale(ShouldRequest shouldRequest) {
                shouldRequest.again(true);
            }
        }).request();

    }

    public void restore(View view) {
        SkinManager.getInstance().loadSkin(this, null);
    }
}
