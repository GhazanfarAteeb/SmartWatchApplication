package com.app.smartwatchapplication.AppConstants;


import android.app.Notification;
import android.location.Location;

import com.app.smartwatchapplication.Modals.City;
import com.app.smartwatchapplication.Modals.GoSafeLoginApiResponse;
import com.app.smartwatchapplication.Modals.WatchReadings;
import com.app.smartwatchapplication.Modals.Weather.Weather;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.polidea.rxandroidble2.RxBleDevice;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final long FASTEST_INTERVAL = 0;
    public static final long INTERVAL = 0;
    public static final String LOGIN_SAVED = null;
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
    public static WatchReadings currentWatchReadings = new WatchReadings();
    public static final String BaseUrl = "http://api.openweathermap.org/";
    public static final String AppId = "714563e4b3022ea7e11842bf0f5af4a4";
    public static final String mode = "JSON";
    public static final String units ="metric";
    public static Weather weatherResponse;
    public static City city;
    public static String GO_SAFE_BASE_URL = "https://gosafeschool.com/";
    public static GoSafeLoginApiResponse USER;
    public static String USER_ID;
    public static boolean IS_ON_BOARDING_SHOWN = false;
    public static final String ON_BOARDING_SHOWN = "ON_BOARDING_SHOWN";
    public static List<LatLng> latLngArrayList = new ArrayList<>();
    public static final int Location_SERVICE_REQUEST_CODE = 123;
    public static final int CREDENTIAL_PICKER_REQUEST = 1;
    public static final int SMS_CONSENT_REQUEST = 2;


}
