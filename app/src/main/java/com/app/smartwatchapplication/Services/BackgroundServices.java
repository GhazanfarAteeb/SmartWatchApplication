package com.app.smartwatchapplication.Services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
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
import com.app.smartwatchapplication.Modals.City;
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
public class BackgroundServices extends Service {
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager notificationManager;
    private PowerManager.WakeLock wakeLock;
    private NotificationCompat.Builder builder;
    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            MapsFragment.map.clear();
            Constants.watchReadingsList = new ArrayList<>();
            Location currentLocation = locationResult.getLastLocation();
            float speedInKMPH = (float) (currentLocation.getSpeed() * 3.6);
            Log.d("CURRENT_SPEED", String.valueOf(speedInKMPH));

            List<LatLng> latLngArrayList = new ArrayList<>();
            for (Location loc : Constants.locationList) {
                latLngArrayList.add(new LatLng(loc.getLatitude(), loc.getLongitude()));
            }
            if (Math.round(speedInKMPH) != 0) {
                Constants.locationList.add(currentLocation);
            }
            MapsFragment.map.addPolyline(new PolylineOptions().addAll(latLngArrayList).width(5).color(Color.BLUE).geodesic(true));
            MapsFragment.tvSpeed.setText(String.format("%.2f", (currentLocation.getSpeed() * 3.6)) + " km/h");
            MapsFragment.tvAccuracy.setText(String.format("%.2f", (currentLocation.getAccuracy())) + "");
            MapsFragment.tvAltitude.setText(String.format("%.2f", (currentLocation.getAltitude())) + "");
            Log.d(null, "================ USER DETAILS ================");
            Log.d("CURRENT_LOCATION : ", currentLocation.getLatitude() + "," + currentLocation.getLongitude());
            Log.d("CURRENT_SPEED : ", String.valueOf(currentLocation.getSpeed()));
            Log.d("CURRENT_ALTITUDE : ", String.valueOf(currentLocation.getAltitude()));
            Log.d("CURRENT_ACCURACY : ", String.valueOf(currentLocation.getAccuracy()));
            Log.d(null, "==============================================");

            if (Constants.weatherResponse == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.BaseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Api service = retrofit.create(Api.class);

                Call<List<City>> cityCall = service.getCityData(String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()), Constants.AppId, "0");
                cityCall.enqueue(new Callback<List<City>>() {
                    @Override
                    public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                     System.out.println("xD RESPONSE_CODE"+ response.code());
                        if (response.code() == 200) {
                            List<City> cityList = response.body();
                            Constants.city = cityList.get(0);
                            System.out.println(Constants.city.getName());
                            System.out.println(Constants.city.getLat());
                            System.out.println(Constants.city.getLon());
                            Call weatherCall = service.getCurrentWeatherData(Constants.city.getLat(), Constants.city.getLon(), Constants.AppId, Constants.mode, Constants.units);
                            weatherCall.enqueue(new Callback() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onResponse(Call call, Response response) {
                                    if (response.code() == 200) {
                                        Constants.weatherResponse = (Weather) response.body();
                                    } else {
                                        System.out.println("Cannot show data");
                                    }
                                }
                                @Override
                                public void onFailure(Call call, Throwable t) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<List<City>> call, Throwable t) {
                        t.getMessage();
                    }
                });
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
                MapsFragment.tvCountry.setText(Constants.weatherResponse.getName() + ", " + Constants.weatherResponse.getSys().getCountry());
                MapsFragment.tvSunrise.setText(getDateString(Constants.weatherResponse.getSys().getSunrise()) + " am");
                MapsFragment.tvSunset.setText(getDateString(Constants.weatherResponse.getSys().getSunset()) + " pm");
            }
            MapsFragment.map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 17.f));
        }
    };

    public static void createLocationRequest() {
        Constants.locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(Constants.INTERVAL)
                .setFastestInterval(Constants.FASTEST_INTERVAL);
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
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
                .setColor(getResources().getColor(R.color.teal_200))
                .setContentTitle(getString(R.string.app_name))
                .setShowWhen(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

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
