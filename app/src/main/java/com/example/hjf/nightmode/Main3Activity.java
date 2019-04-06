package com.example.hjf.nightmode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
    }

    public void onClick(View view) {
        boolean b = SharedPreferencesUtil.getInsance().getNightMode();
        NightModeManager.getInstance().nightModeChange(!b);
    }
}
