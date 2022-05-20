package com.app.smartwatchapplication.AppConstants;


import android.app.Notification;
import android.location.Location;

import com.app.smartwatchapplication.Modals.WatchReadings;
import com.app.smartwatchapplication.Modals.Weather.Weather;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.polidea.rxandroidble2.RxBleDevice;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final long FASTEST_INTERVAL = 0;
    public static final long INTERVAL = 0;
    public static boolean isFineLocationPermissionGranted = false;
    public static boolean isCoarseLocationPermissionGranted = false;
    public static boolean isBluetoothPermissionGranted = false;
    public static boolean isBluetoothAdminPermissionGranted = false;
    public static boolean isBluetoothScanPermissionGranted = false;
    public static boolean isBluetoothConnectPermissionGranted = false;
    public static final String NOTIFICATION_ID = "serviceChannel";
    public static LocationRequest locationRequest;
    public static FusedLocationProviderClient client;
    public static List<Location> locationList;
    public static Notification notification;
    public static RxBleDevice connectedDevice = null;
    public static List<WatchReadings> watchReadingsList = new ArrayList<>();
    public static WatchReadings currentWatchReadings = new WatchReadings();
    public static boolean isWeatherFetched = false;
    public static String BaseUrl = "http://api.openweathermap.org/";
    public static String AppId = "714563e4b3022ea7e11842bf0f5af4a4";
    public static String lat = "35";
    public static String lon = "139";
    public static String mode = "JSON";
    public static String units ="metric";
    public static Weather weatherResponse;
}
