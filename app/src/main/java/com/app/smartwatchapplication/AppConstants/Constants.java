package com.app.smartwatchapplication.AppConstants;


import android.app.Notification;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.polidea.rxandroidble2.RxBleDevice;

import java.util.List;

public class Constants {
    public static final long FASTEST_INTERVAL = 0;
    public static final long INTERVAL = 500;
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
}
