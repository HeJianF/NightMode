package com.example.hjf.nightmode;

import android.os.Bundle;
import android.view.View;

public class Main2Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void onClick(View view) {
        boolean b = SharedPreferencesUtil.getInsance().getNightMode();
        NightModeManager.getInstance().nightModeChange(!b);
    }
}
