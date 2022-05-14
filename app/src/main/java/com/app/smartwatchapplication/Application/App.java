package com.app.smartwatchapplication.Application;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.htsmart.wristband2.WristbandApplication;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        WristbandApplication.init(App.this);
        WristbandApplication.setDebugEnable(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}
