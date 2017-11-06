package com.example.admin.gamelf_regsoft;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class load extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        CountDownTimer Timer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                finish();
            }
        }.start();
    }
}
