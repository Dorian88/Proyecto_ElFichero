package com.elfichero.elfichero;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;


public class Cargar extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar);


        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
                startService(new Intent(getBaseContext(), RefreshService.class));
            }
            public void onFinish() {
                startActivity(new Intent(getBaseContext(), ResultActivity.class));
            }
        }.start();
    }
}
