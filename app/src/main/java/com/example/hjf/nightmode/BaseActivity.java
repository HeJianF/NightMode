package com.example.hjf.nightmode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Jianfeng He
 * @email hjfstory@foxmail.com
 * @date 2018-12-31
 */
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    private boolean mIsMockViewAdd = false;
    private View mMockView;
    private NightModeManager nightModeManager;
    private NightModeManager.NightModeChangeListener nightModeChangeListener = new NightModeManager.NightModeChangeListener() {
        @Override
        public void onMockColorChange(int color) {
            if (mMockView != null) {
                Log.d(TAG, "onMockColorChange: " + color);
                mMockView.setBackgroundColor(color);
            }
        }

        @Override
        public void onNightModeChange(boolean nightMode) {
            setNightModeView(nightMode);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nightModeManager = NightModeManager.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setNightModeView(nightModeManager.isNightMode());
        nightModeManager.addListener(nightModeChangeListener);
    }

    private void setNightModeView(boolean nightMode) {
        if (nightMode) {
            if (mMockView == null) {
                initMockView();
            }
            if (!mIsMockViewAdd) {
                mIsMockViewAdd = true;
                mMockView.setBackgroundColor(nightModeManager.getColor());
                ((ViewGroup) getWindow().getDecorView()).addView(mMockView, new ViewGroup.LayoutParams(-1, -1));
            }
        } else if (mIsMockViewAdd) {
            mIsMockViewAdd = false;
            ((ViewGroup) getWindow().getDecorView()).removeView(mMockView);
        }
    }

    private void initMockView() {
        if (mMockView == null) {
            mMockView = NightModeManager.inflateView();
            mMockView.setClickable(false);
            mMockView.setFocusable(false);
            mMockView.setFocusableInTouchMode(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        nightModeManager.removeListener(nightModeChangeListener);
    }
}
