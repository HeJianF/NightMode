package com.example.hjf.nightmode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Jianfeng He
 * @email hjfstory@foxmail.com
 * @date 2018-12-31
 */
public class SharedPreferencesUtil {

    private static SharedPreferencesUtil instance = null;

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    private static final String IS_NIGHT_MODE = "isNightMode";

    @SuppressLint("CommitPrefEdits")
    private SharedPreferencesUtil() {
        preferences = MyApplication.getContext().getSharedPreferences("night", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static SharedPreferencesUtil getInsance() {
        if (instance == null) {
            return new SharedPreferencesUtil();
        }
        return instance;
    }

    public boolean getNightMode() {
        return preferences.getBoolean(IS_NIGHT_MODE, false);
    }

    public void saveNightMode(boolean nightMode) {
        editor.putBoolean(IS_NIGHT_MODE, nightMode).apply();
    }
}
