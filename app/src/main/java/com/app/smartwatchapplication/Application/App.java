package com.app.smartwatchapplication.Application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.provider.SyncStateContract;

import androidx.appcompat.app.AppCompatDelegate;

import com.app.smartwatchapplication.AppConstants.Constants;
import com.htsmart.wristband2.WristbandApplication;

public class App extends Application {
    public static NotificationManager notificationManager;
    @Override
    public void onCreate() {
        super.onCreate();
        WristbandApplication.init(App.this);
        WristbandApplication.setDebugEnable(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        createNotificationChannel();
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    Constants.NOTIFICATION_ID,
                    "Notification service",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            notificationManager =getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(serviceChannel);
            notificationManager.cancelAll();
        }
    }
}
