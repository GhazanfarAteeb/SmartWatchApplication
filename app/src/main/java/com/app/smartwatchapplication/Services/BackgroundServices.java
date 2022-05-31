package com.app.smartwatchapplication.Services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.app.smartwatchapplication.Activities.ActivityMain;
import com.app.smartwatchapplication.Activities.ui.maps.MapsFragment;
import com.app.smartwatchapplication.Apis.Api;
import com.app.smartwatchapplication.AppConstants.Constants;
import com.app.smartwatchapplication.DB.DatabaseHelper;
import com.app.smartwatchapplication.Listeners.DbThread;
import com.app.smartwatchapplication.Modals.City;
import com.app.smartwatchapplication.Modals.PostReadings;
import com.app.smartwatchapplication.Modals.PostReadingsResponse;
import com.app.smartwatchapplication.Modals.Weather.Weather;
import com.app.smartwatchapplication.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressLint({"SetTextI18n", "DefaultLocale"})
public class BackgroundServices extends Service implements DbThread.DBThreadListener {
    int i = 0;
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager notificationManager;
    private PowerManager.WakeLock wakeLock;
    private NotificationCompat.Builder builder;
    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            MapsFragment.map.clear();
            Location currentLocation = locationResult.getLastLocation();
            float speedInKMPH = (float) (currentLocation.getSpeed() * 3.6);
            Constants.latLngArrayList.add(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
            MapsFragment.tvSpeed.setText(String.format("%.2f", (currentLocation.getSpeed() * 3.6)) + " km/h");
            MapsFragment.tvAccuracy.setText(String.format("%.2f", (currentLocation.getAccuracy())) + "");
            MapsFragment.tvAltitude.setText(String.format("%.2f", (currentLocation.getAltitude())) + "");

            Log.d("", "================ USER DETAILS ================");
            Log.d("CURRENT_LOCATION : ", currentLocation.getLatitude() + "," + currentLocation.getLongitude());
            Log.d("CURRENT_SPEED : ", String.valueOf(currentLocation.getSpeed()));
            Log.d("CURRENT_ALTITUDE : ", String.valueOf(currentLocation.getAltitude()));
            Log.d("CURRENT_ACCURACY : ", String.valueOf(currentLocation.getAccuracy()));
            Log.d("CURRENT_BEARING : ", String.valueOf(currentLocation.getBearing()));
            Log.d("", "==============================================");

            if (Constants.weatherResponse == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.BaseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Api service = retrofit.create(Api.class);
                Call<List<City>> cityCall = service.getCityData(String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()), Constants.AppId, "0");
                cityCall.enqueue(new Callback<List<City>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<City>> call, @NonNull Response<List<City>> response) {
                        System.out.println("   RESPONSE_CODE" + response.code());
                        if (response.code() == 200) {
                            List<City> cityList = response.body();
                            assert cityList != null;
                            Constants.city = cityList.get(0);
                            Call<Weather> weatherCall = service.getCurrentWeatherData(Constants.city.getLat(), Constants.city.getLon(), Constants.AppId, Constants.mode, Constants.units);
                            weatherCall.enqueue(new Callback<Weather>() {
                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) {
                                    if (response.code() == 200) {
                                        Constants.weatherResponse = (Weather) response.body();
                                    } else {
                                        System.out.println("Cannot show data");
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call call, @NonNull Throwable t) {

                                }
                            });
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<List<City>> call, @NonNull Throwable t) {
                        t.getLocalizedMessage();
                    }
                });
            }
            //DATABASE HELPER CLASS
            DatabaseHelper dbHelper = new DatabaseHelper(BackgroundServices.this);

            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            //IF MY NETWORK IS AVAILABLE
            if (networkInfo != null && networkInfo.isConnected()) {
                // READ THE SQLITE DATABASE
                Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                        "SELECT * FROM " + Constants.READINGS_TABLE_NAME + " WHERE "
                                + Constants.READINGS_STATUS + "=?",
                        new String[]{
                                String.valueOf(Constants.STATUS_NOT_UPLOADED)
                        }
                );

                // IF TABLE SEQUENCES ARE CHANGED THEN THIS WILL GET THE INDEX ACCORDING TO THE CHANGED INDEXES
                int locationReadingsID = cursor.getColumnIndex(Constants.READINGS_ID);
                int locationReadingsLat = cursor.getColumnIndex(Constants.READINGS_LAT);
                int locationReadingsLon = cursor.getColumnIndex(Constants.READINGS_LON);
                int locationReadingsTimestamp = cursor.getColumnIndex(Constants.READINGS_TIMESTAMP);
                int locationReadingsAltitude = cursor.getColumnIndex(Constants.READINGS_ALTITUDE);
                int locationReadingsSpeed = cursor.getColumnIndex(Constants.READINGS_SPEED);
                int locationReadingsBearing = cursor.getColumnIndex(Constants.READINGS_BEARING);
                int locationReadingsAccuracy = cursor.getColumnIndex(Constants.READINGS_ACCURACY);
                int locationReadingsTemp = cursor.getColumnIndex(Constants.READINGS_TEMP);
                int locationReadingsFeelsLike = cursor.getColumnIndex(Constants.READINGS_FEELS_LIKE);
                int locationReadingsTempMax = cursor.getColumnIndex(Constants.READINGS_TEMP_MAX);
                int locationReadingsTempMin = cursor.getColumnIndex(Constants.READINGS_TEMP_MIN);
                int locationReadingsPressure = cursor.getColumnIndex(Constants.READINGS_PRESSURE);
                int locationReadingsHumidity = cursor.getColumnIndex(Constants.READINGS_HUMIDITY);
                int locationReadingsWind = cursor.getColumnIndex(Constants.READINGS_WIND);
                int locationReadingsClouds = cursor.getColumnIndex(Constants.READINGS_CLOUDS);
                int locationReadingsVisibility = cursor.getColumnIndex(Constants.READINGS_VISIBILITY);
                int locationReadingsSystolicBP = cursor.getColumnIndex(Constants.READINGS_SYSTOLIC_BLOOD_PRESSURE);
                int locationReadingsDiastolicBP = cursor.getColumnIndex(Constants.READINGS_DIASTOLIC_BLOOD_PRESSURE);
                int locationReadingsHeartRate = cursor.getColumnIndex(Constants.READINGS_HEART_RATE);
                int locationReadingsSPO2 = cursor.getColumnIndex(Constants.READINGS_BLOOD_OXYGEN);
                int locationReadingsRespirationRate = cursor.getColumnIndex(Constants.READINGS_RESPIRATION_RATE);
                int locationReadingsStatus = cursor.getColumnIndex(Constants.READINGS_STATUS);
                ArrayList<PostReadings> postReadingsArrayList = new ArrayList<>();

                // GETTING DATA FROM THE CURSOR
                while (cursor.moveToNext()) {
                    postReadingsArrayList.add(
                            new PostReadings(
                                    cursor.getString(locationReadingsID),
                                    cursor.getString(locationReadingsLat),
                                    cursor.getString(locationReadingsLon),
                                    cursor.getString(locationReadingsTimestamp),
                                    cursor.getString(locationReadingsAltitude),
                                    cursor.getString(locationReadingsSpeed),
                                    cursor.getString(locationReadingsBearing),
                                    cursor.getString(locationReadingsAccuracy),
                                    cursor.getString(locationReadingsTemp),
                                    cursor.getString(locationReadingsFeelsLike),
                                    cursor.getString(locationReadingsTempMin),
                                    cursor.getString(locationReadingsTempMax),
                                    cursor.getString(locationReadingsPressure),
                                    cursor.getString(locationReadingsHumidity),
                                    cursor.getString(locationReadingsWind),
                                    cursor.getString(locationReadingsClouds),
                                    cursor.getString(locationReadingsVisibility),
                                    cursor.getString(locationReadingsSystolicBP),
                                    cursor.getString(locationReadingsDiastolicBP),
                                    cursor.getString(locationReadingsHeartRate),
                                    cursor.getString(locationReadingsSPO2),
                                    cursor.getString(locationReadingsRespirationRate),
                                    cursor.getInt(locationReadingsStatus)
                            )
                    );
                }
                cursor.close();
                new DbThread(BackgroundServices.this, postReadingsArrayList, dbHelper).start();
            }

            if (speedInKMPH >= 10) {
                PostReadings postReadings = new PostReadings(
                        Constants.USER_ID,
                        String.valueOf(currentLocation.getLatitude()),
                        String.valueOf(currentLocation.getLongitude()),
                        String.valueOf(System.currentTimeMillis()),
                        String.valueOf(currentLocation.getAltitude()),
                        String.valueOf(currentLocation.getSpeed()),
                        String.valueOf(currentLocation.getBearing()),
                        String.valueOf(currentLocation.getAccuracy()),
                        String.valueOf(Constants.weatherResponse.getMain().getTemp()),
                        String.valueOf(Constants.weatherResponse.getMain().getFeelsLike()),
                        String.valueOf(Constants.weatherResponse.getMain().getTempMin()),
                        String.valueOf(Constants.weatherResponse.getMain().getTempMax()),
                        String.valueOf(Constants.weatherResponse.getMain().getPressure()),
                        String.valueOf(Constants.weatherResponse.getMain().getHumidity()),
                        String.valueOf(Constants.weatherResponse.getWind().getSpeed()),
                        String.valueOf(Constants.weatherResponse.getClouds().getAll()),
                        String.valueOf(Constants.weatherResponse.getVisibility()),
                        String.valueOf(Constants.currentWatchReadings.getSystolicBloodPressure()),
                        String.valueOf(Constants.currentWatchReadings.getDiastolicBloodPressure()),
                        String.valueOf(Constants.currentWatchReadings.getHeartRate()),
                        String.valueOf(Constants.currentWatchReadings.getBloodOxygenLevel()),
                        String.valueOf(Constants.currentWatchReadings.getRespirationRate()),
                        Constants.STATUS_NOT_UPLOADED
                );

                ContentValues contentValues = new ContentValues();
                contentValues.put(Constants.READINGS_ID, postReadings.getId());
                contentValues.put(Constants.READINGS_LAT, postReadings.getLat());
                contentValues.put(Constants.READINGS_LON, postReadings.getLon());
                contentValues.put(Constants.READINGS_TIMESTAMP, postReadings.getTimestamp());
                contentValues.put(Constants.READINGS_ALTITUDE, postReadings.getAltitude());
                contentValues.put(Constants.READINGS_SPEED, postReadings.getSpeed());
                contentValues.put(Constants.READINGS_BEARING, postReadings.getBearing());
                contentValues.put(Constants.READINGS_ACCURACY, postReadings.getAccuracy());
                contentValues.put(Constants.READINGS_TEMP, postReadings.getTemp());
                contentValues.put(Constants.READINGS_FEELS_LIKE, postReadings.getFeelsLike());
                contentValues.put(Constants.READINGS_TEMP_MAX, postReadings.getTempMax());
                contentValues.put(Constants.READINGS_TEMP_MIN, postReadings.getTempMin());
                contentValues.put(Constants.READINGS_PRESSURE, postReadings.getPressure());
                contentValues.put(Constants.READINGS_HUMIDITY, postReadings.getHumidity());
                contentValues.put(Constants.READINGS_WIND, postReadings.getWind());
                contentValues.put(Constants.READINGS_CLOUDS, postReadings.getClouds());
                contentValues.put(Constants.READINGS_VISIBILITY, postReadings.getVisibility());
                contentValues.put(Constants.READINGS_SYSTOLIC_BLOOD_PRESSURE, postReadings.getSystolicBP());
                contentValues.put(Constants.READINGS_DIASTOLIC_BLOOD_PRESSURE, postReadings.getDiastolicBP());
                contentValues.put(Constants.READINGS_HEART_RATE, postReadings.getHeartRate());
                contentValues.put(Constants.READINGS_BLOOD_OXYGEN, postReadings.getSpo2());
                contentValues.put(Constants.READINGS_RESPIRATION_RATE, postReadings.getRespirationRate());
                contentValues.put(Constants.READINGS_STATUS, postReadings.getStatus());

                dbHelper.getWritableDatabase().insert(Constants.READINGS_TABLE_NAME, null, contentValues);
                Constants.locationList.add(currentLocation);
            }
            if (Constants.weatherResponse != null) {
                MapsFragment.tvWeather.setText(Constants.weatherResponse.getWeather().get(0).getMain());
                MapsFragment.tvWindSpeed.setText(Constants.weatherResponse.getWind().getSpeed() + " km/h");
                MapsFragment.tvHumidity.setText(Constants.weatherResponse.getMain().getHumidity() + "%");
                MapsFragment.tvClouds.setText(Constants.weatherResponse.getClouds().getAll() + "%");
                if (Constants.weatherResponse.getVisibility() >= 1000) {
                    MapsFragment.tvVisibility.setText((Constants.weatherResponse.getVisibility() / 1000) + " km");
                } else {
                    MapsFragment.tvVisibility.setText((Constants.weatherResponse.getVisibility() / 1000) + " m");
                }
                MapsFragment.tvTemperature.setText(Constants.weatherResponse.getMain().getTemp() + "°C");
                MapsFragment.tvMinTemperature.setText(Constants.weatherResponse.getMain().getTempMin() + "°C");
                MapsFragment.tvMaxTemperature.setText(Constants.weatherResponse.getMain().getTempMax() + "°C");
                MapsFragment.tvTemperatureFeelsLike.setText(Constants.weatherResponse.getMain().getFeelsLike() + "°C");
                MapsFragment.tvCountry.setText(Constants.weatherResponse.getName() + ", " + Constants.weatherResponse.getSys().getCountry());
                MapsFragment.tvSunrise.setText(getDateString(Constants.weatherResponse.getSys().getSunrise()) + " am");
                MapsFragment.tvSunset.setText(getDateString(Constants.weatherResponse.getSys().getSunset()) + " pm");
            }
            MapsFragment.map.addPolyline(new PolylineOptions().addAll(Constants.latLngArrayList).width(8).color(Color.BLUE).geodesic(true));
            MapsFragment.map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 16.f));
        }
    };

    public static void createLocationRequest() {
        Constants.locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(Constants.INTERVAL)
                .setFastestInterval(Constants.FASTEST_INTERVAL);
    }


    @Override
    public void updateDB(List<PostReadings> postReadingsList, DatabaseHelper dbHelper) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.GO_SAFE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Api service = retrofit.create(Api.class);
            for (PostReadings reading : postReadingsList) {
                Call<PostReadingsResponse> readingsCall = service.postAllData(
                        reading.getId(),
                        reading.getLat(),
                        reading.getLon(),
                        reading.getTimestamp(),
                        reading.getAltitude(),
                        reading.getSpeed(),
                        reading.getBearing(),
                        reading.getAccuracy(),
                        reading.getTemp(),
                        reading.getFeelsLike(),
                        reading.getTempMin(),
                        reading.getTempMax(),
                        reading.getPressure(),
                        reading.getHumidity(),
                        reading.getWind(),
                        reading.getClouds(),
                        reading.getVisibility(),
                        reading.getSystolicBP(),
                        reading.getDiastolicBP(),
                        reading.getHeartRate(),
                        reading.getSpo2(),
                        reading.getRespirationRate()
                );

                Response<PostReadingsResponse> response = readingsCall.execute();
                System.out.println("POST_READINGS_RESPONSE" + response.code());
                if (response.code() == 200) {
                    PostReadingsResponse postReadingsResponse = response.body();
                    assert postReadingsResponse != null;
                    System.out.println(postReadingsResponse.success);
                    if (postReadingsList.size() > 0) {
                        dbHelper.getWritableDatabase().execSQL(
                                "UPDATE " + Constants.READINGS_TABLE_NAME + " SET " + Constants.READINGS_STATUS + "=" + Constants.STATUS_UPLOADED +
                                        " WHERE " +
                                        Constants.READINGS_ID + "='" + reading.getId() + "' AND " +
                                        Constants.READINGS_TIMESTAMP + "='" + reading.getTimestamp() + "'");
                    }
                }
            }
        } catch (Exception ex) {
            ex.getLocalizedMessage();
        }
    }

    public class LocalBinder extends Binder {
        public BackgroundServices getServiceInstance() {
            return BackgroundServices.this;
        }
    }

    private final IBinder locationServiceBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (wakeLock != null && !wakeLock.isHeld()) {
            wakeLock.acquire();
        }
        return locationServiceBinder;
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Constants.client.requestLocationUpdates(Constants.locationRequest,
                this.locationCallback, Looper.getMainLooper());
    }

    @Override
    public void onDestroy() {
        Constants.client.removeLocationUpdates(locationCallback);
        Log.d("STOP_SERVICE", "TRYING TO STOP SERVICE FROM ON DESTROY");
        stopSelf();
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Constants.client.removeLocationUpdates(locationCallback);
        stopSelf();
        Log.d("STOP_SERVICE", "TRYING TO STOP SERVICE");
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Constants.client = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(getApplicationContext());
        createLocationRequest();
        startLocationUpdates();
        Constants.notification = getNotification();
        startForeground(NOTIFICATION_ID, Constants.notification);
        Log.d("SERVICE", "ON START COMMAND");

        return START_NOT_STICKY;
    }

    private Notification getNotification() {
        final String CHANNEL_ID = "serviceChannel";

        builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        //builder.setSmallIcon(R.drawable.ic_notification_24dp)
        builder.setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(getString(R.string.app_name))
                .setShowWhen(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentText("Go Safe School is running in background.");

        final Intent startIntent = new Intent(getApplicationContext(), ActivityMain.class);
        startIntent.setAction(Intent.ACTION_MAIN);
        startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 1, startIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(contentIntent);
        return builder.build();
    }

    @SuppressLint("SimpleDateFormat")
    private String getDateString(long timeInMilliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(timeInMilliseconds);
    }
}
