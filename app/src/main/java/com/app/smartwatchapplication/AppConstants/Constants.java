package com.app.smartwatchapplication.AppConstants;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;

public class Constants {
    public static final long FASTEST_INTERVAL = 0;
    public static final long INTERVAL = 500;
    public static boolean isFineLocationPermissionGranted = false;
    public static boolean isCoarseLocationPermissionGranted = false;
    public static boolean isBluetoothPermissionGranted = false;
    public static boolean isBluetoothAdminPermissionGranted = false;
    public static boolean isBluetoothScanPermissionGranted = false;
    public static boolean isBluetoothConnectPermissionGranted = false;
    public static final String NOTIFICATION_ID = "notificationServiceChannel";
    public static LocationRequest locationRequest;
    public static FusedLocationProviderClient client;
}
