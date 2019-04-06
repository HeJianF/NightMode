package com.example.hjf.nightmode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity {

    boolean b = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_night_mode:
                boolean b = SharedPreferencesUtil.getInsance().getNightMode();
                NightModeManager.getInstance().nightModeChange(!b);
                break;
            case R.id.bt_act:
                startActivity(new Intent(this, Main2Activity.class));
                break;
            case R.id.bt_other_act:
                startActivity(new Intent(this, Main3Activity.class));
                break;
            case R.id.bt_dialog:

                break;
            default:
        }
    }
}
