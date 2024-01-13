package com.enjoy.hotfix;

import android.app.Application;
import android.content.Context;

import com.enjoy.patch.EnjoyFix;

import java.io.File;


public class MyApplication extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        EnjoyFix.installPatch(this, new File("/sdcard/patch.jar"));
    }
}
