package com.example.hjf.nightmode;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * @author Jianfeng He
 * @email hjfstory@foxmail.com
 * @date 2018-12-31
 */
public class MyApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        NightModeManager.getInstance();
    }

    public static Context getContext() {
        return context;
    }
}
