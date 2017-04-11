package com.dobe.zer0.coolweather.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by dobezer0 on 2017/4/10.
 *
 * github url: https://github.com/DobeChen/coolweather.git
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Activity >>>> ", getClass().toString());
    }
}
