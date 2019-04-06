package com.example.hjf.nightmode;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings.System;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jianfeng He
 * @email hjfstory@foxmail.com
 * @date 2018-12-31
 */
public class NightModeManager {

    private static final String TAG = "NightModeManager";

    private static volatile NightModeManager nightModeManager = null;

    private static final Uri SCREEN_BRIGHTNESS_MODE = System.getUriFor("screen_brightness_mode");
    private static final Uri SCREEN_BRIGHTNESS = System.getUriFor("screen_brightness");
    private static final Uri SCREEN_AUTO_BRIGHTNESS_ADJ = System.getUriFor("screen_auto_brightness_adj");

    private static final int NUM_216 = 216;
    private static final int NUM_25 = 25;
    private static final int NUM_1118481 = 1118481;

    /**
     * 是否是夜间模式
     */
    private boolean mIsNightMode;

    /**
     * 是否已经注册
     */
    private boolean mIsRegister;

    private WeakReference<ContentResolver> mWeakReference;

    private ContentObserver mContentObserver = new ContentObserver(new Handler()) {

        @Override
        public void onChange(boolean selfChange) {
            this.onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange);
            if (!selfChange) {
                if (SCREEN_BRIGHTNESS_MODE.equals(uri)) {
                    Log.d(TAG, "onChange: 1");
                    nightModeManager.notificationChange();
                } else if (SCREEN_BRIGHTNESS.equals(uri) && !mode()) {
                    Log.d(TAG, "onChange: 2");
                    nightModeManager.notificationChange();
                } else if (SCREEN_AUTO_BRIGHTNESS_ADJ.equals(uri) && mode()) {
                    Log.d(TAG, "onChange: 3");
                    nightModeManager.notificationChange();
                } else {
                    Log.d(TAG, "onChange: ");
                    nightModeManager.notificationChange();
                }
            }
        }
    };

    private NightModeManager() {
        mIsNightMode = SharedPreferencesUtil.getInsance().getNightMode();
        //StatusBarManager.setIsNightMode(mIsNightMode);
        mWeakReference = new WeakReference(MyApplication.getContext().getContentResolver());
        if (mIsNightMode) {
            register();
        }
    }

    public static NightModeManager getInstance() {
        if (nightModeManager == null) {
            synchronized (NightModeManager.class) {
                if (nightModeManager == null) {
                    nightModeManager = new NightModeManager();
                }
            }
        }
        return nightModeManager;
    }

    public void nightModeChange(boolean nightMode) {
        if (nightMode != mIsNightMode) {
            mIsNightMode = nightMode;
            //StatusBarManager.setIsNightMode(mIsNightMode);
            SharedPreferencesUtil.getInsance().saveNightMode(nightMode);
            if (mIsNightMode) {
                register();
            } else {
                unReghiter();
            }
            if (listeners != null) {
                Log.d(TAG, "nightModeChange: " + listeners.size());
                for (NightModeChangeListener listener : listeners) {
                    listener.onNightModeChange(mIsNightMode);
                }
            }
        }
    }

    public boolean isNightMode() {
        return mIsNightMode;
    }

    private List<NightModeChangeListener> listeners = new ArrayList<>();

    public interface NightModeChangeListener {
        /**
         * 蒙层颜变化
         *
         * @param color 颜色
         */
        void onMockColorChange(int color);

        /**
         * 夜间模式状态发生变化
         *
         * @param nightMode 夜间模式状态
         */
        void onNightModeChange(boolean nightMode);
    }

    /**
     * 当屏幕亮度发生变化，对蒙版的颜色进行相应的调整
     */
    private void notificationChange() {
        if (listeners != null && mIsNightMode) {
            int color = getColor();
            for (NightModeChangeListener listener : listeners) {
                listener.onMockColorChange(color);
            }
        }
    }

    /**
     * 获取为mock设置的颜色
     *
     * @return
     */
    public int getColor() {
        return ((((int) ((((float) mockColor()) / 255.0f) * 191.0f)) + 25) << 24) + NUM_1118481;
    }

    private int mockColor() {
        if (mode()) {
            return adj();
        }
        return brightness();
    }

    /**
     * 自动调节亮度 = 1
     * 手动调节亮度 = 0
     */
    private boolean mode() {
        try {
            ContentResolver contentResolver = mWeakReference.get();
            if (contentResolver != null) {
                return System.getInt(contentResolver, "screen_brightness_mode") == 1;
            }
        } catch (Throwable e) {
            Log.e(TAG, "mode: ", e);
        }
        return false;
    }

    private int adj() {
        float f;
        try {
            ContentResolver contentResolver = mWeakReference.get();
            if (contentResolver != null) {
                f = System.getFloat(contentResolver, "screen_auto_brightness_adj");
                return (int) (((f + 1.0f) / 2.0f) * 225.0f);
            }
        } catch (Throwable e) {
            Log.e(TAG, "adj: ", e);
        }
        f = 0.0f;
        return (int) (((f + 1.0f) / 2.0f) * 225.0f);
    }

    private int brightness() {
        try {
            ContentResolver contentResolver = mWeakReference.get();
            if (contentResolver != null) {
                return System.getInt(contentResolver, "screen_brightness");
            }
        } catch (Throwable e) {
            Log.e(TAG, "brightness: ", e);
        }
        return 0;
    }

    private void register() {
        try {
            if (mContentObserver != null && !mIsRegister) {
                ContentResolver contentResolver = mWeakReference.get();
                if (contentResolver != null) {
                    contentResolver.registerContentObserver(SCREEN_BRIGHTNESS_MODE, false, mContentObserver);
                    contentResolver.registerContentObserver(SCREEN_BRIGHTNESS, false, mContentObserver);
                    contentResolver.registerContentObserver(SCREEN_AUTO_BRIGHTNESS_ADJ, false, mContentObserver);
                    mIsRegister = true;
                }
            }
        } catch (Throwable e) {
            Log.e(TAG, "register: " + e);
        }
    }

    private void unReghiter() {
        try {
            ContentResolver resolver = mWeakReference.get();
            if (resolver != null) {
                resolver.unregisterContentObserver(mContentObserver);
                mIsRegister = false;
            }
        } catch (Throwable e) {
            Log.e(TAG, "unReghiter: ", e);
        }
    }

    public void addListener(NightModeChangeListener listener) {
        if (!listeners.contains(listener) && listener != null) {
            listeners.add(listener);
        }
    }

    public void removeListener(NightModeChangeListener listener) {
        if (listeners.contains(listener) && listener != null) {
            listeners.remove(listener);
        }
    }

    public static View inflateView() {
        return LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.night_mode, null);
    }

    public static void setManagerNull() {
        nightModeManager = null;
    }
}
