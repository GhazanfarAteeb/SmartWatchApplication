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
    public static boolean IS_JOURNEY_STARTED = false;
    public static final int Location_SERVICE_REQUEST_CODE = 1;
    public static long startTime = 0L;


    //DATABASE ATTRIBUTES
    public static final String DB_NAME = "gss.db";
    public static final int DB_VERSION = 1;
    public static final String READINGS_TABLE_NAME = "READINGS";
    public static final String READINGS_ID = "ID";
    public static final String READINGS_LAT = "LAT";
    public static final String READINGS_LON = "LON";
    public static final String READINGS_TIMESTAMP = "TIMESTAMP";
    public static final String READINGS_ALTITUDE = "ALTITUDE";
    public static final String READINGS_ACCURACY = "ACCURACY";
    public static final String READINGS_SPEED = "SPEED";
    public static final String READINGS_BEARING = "BEARING";
    public static final String READINGS_TEMP = "TEMPERATURE";
    public static final String READINGS_FEELS_LIKE = "FEELS_LIKE";
    public static final String READINGS_TEMP_MAX = "MAXIMUM_TEMPERATURE";
    public static final String READINGS_TEMP_MIN = "MINIMUM_TEMPERATURE";
    public static final String READINGS_PRESSURE = "PRESSURE";
    public static final String READINGS_HUMIDITY = "HUMIDITY";
    public static final String READINGS_WIND = "WIND";
    public static final String READINGS_CLOUDS = "CLOUDS";
    public static final String READINGS_VISIBILITY = "VISIBILITY";
    public static final String READINGS_SYSTOLIC_BLOOD_PRESSURE = "SYSTOLIC_BLOOD_PRESSURE";
    public static final String READINGS_DIASTOLIC_BLOOD_PRESSURE = "DIASTOLIC_BLOOD_PRESSURE";
    public static final String READINGS_HEART_RATE = "HEART_RATE";
    public static final String READINGS_BLOOD_OXYGEN = "BLOOD_OXYGEN";
    public static final String READINGS_RESPIRATION_RATE = "RESPIRATION_RATE";
    public static final String READINGS_STATUS = "STATUS";
    public static final int STATUS_UPLOADED = 1;
    public static final int STATUS_NOT_UPLOADED = 0;
}
