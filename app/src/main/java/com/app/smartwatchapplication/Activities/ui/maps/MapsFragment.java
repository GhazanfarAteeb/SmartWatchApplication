package com.app.smartwatchapplication.Activities.ui.maps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.app.smartwatchapplication.Activities.WatchScanActivity;
import com.app.smartwatchapplication.AppConstants.Constants;
import com.app.smartwatchapplication.R;
import com.app.smartwatchapplication.Services.BackgroundServices;
import com.app.smartwatchapplication.databinding.FragmentMapsBinding;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

@SuppressLint({"SetTextI18n", "StaticFieldLeak"})
public class MapsFragment extends Fragment implements OnMapReadyCallback {
    public static GoogleMap map;
    View root;
    SupportMapFragment mapsFragment;
    public static Intent serviceIntent;
    public static TextView tvJourneyStartedAt, tvJourneyTime, tvCountry, tvSunrise, tvSunset, tvBloodPressure, tvHeartRate,
            tvBloodOxygen, tvRespirationRate, tvWeather, tvWindSpeed, tvHumidity, tvClouds, tvVisibility, tvTemperature, tvMinTemperature, tvMaxTemperature, tvTemperatureFeelsLike,
            tvSpeed, tvAccuracy, tvAltitude;
    ImageView ivStart, ivStop;
    public static final Handler customHandler = new Handler();
    public static long timeInSeconds = 0L;
    public static long timeSwapBuff = 0L;
    public static long updatedTime = 0L;

    public static Runnable updateTimeThread = new Runnable() {

        @Override
        public void run() {
            timeInSeconds = System.currentTimeMillis() - Constants.startTime;
            updatedTime = timeSwapBuff + timeInSeconds;
            int seconds = (int) (updatedTime / 1000);
            int minutes = seconds / 60;
            int hours = seconds / 3600;

            seconds = seconds % 60;
            minutes %= 60;

            tvJourneyTime.setText(
                    String.format(Locale.getDefault(), "%02d", hours) + ":" +
                    String.format(Locale.getDefault(), "%02d", minutes)+":" +
                    String.format(Locale.getDefault(), "%02d", seconds)
            );
            customHandler.post(this);
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FragmentMapsBinding binding = FragmentMapsBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        mapsFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
        return root;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        if (mapsFragment != null) {
            mapsFragment.getMapAsync(this);
        }
        if(Constants.connectedDevice == null) {
            Intent intent = new Intent(getActivity(), WatchScanActivity.class);
            startActivity(intent);
        }
    }

    public static com.app.smartwatchapplication.Services.BackgroundServices locationServices;
    public static boolean isGPSServiceBound = false;
    public static final ServiceConnection GPSServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BackgroundServices.LocalBinder binder = (BackgroundServices.LocalBinder) iBinder;
            locationServices = binder.getServiceInstance();
            isGPSServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    private void init() {
        tvJourneyStartedAt = root.findViewById(R.id.tv_started_at);
        tvJourneyTime = root.findViewById(R.id.tv_journey_time);
        tvCountry = root.findViewById(R.id.tv_country);
        tvSunrise = root.findViewById(R.id.tv_sunrise);
        tvSunset = root.findViewById(R.id.tv_sunset);
        tvBloodPressure = root.findViewById(R.id.tv_bp);
        tvHeartRate = root.findViewById(R.id.tv_hr);
        tvBloodOxygen = root.findViewById(R.id.tv_spo2);
        tvRespirationRate = root.findViewById(R.id.tv_respiration_rate);
        tvWeather = root.findViewById(R.id.tv_weather);
        tvWindSpeed = root.findViewById(R.id.tv_wind_speed);
        tvHumidity = root.findViewById(R.id.tv_humidity);
        tvClouds = root.findViewById(R.id.tv_clouds);
        tvVisibility = root.findViewById(R.id.tv_visibility);
        tvTemperature = root.findViewById(R.id.tv_temperature);
        tvMinTemperature = root.findViewById(R.id.tv_minimum_temperature);
        tvMaxTemperature = root.findViewById(R.id.tv_maximum_temperature);
        tvTemperatureFeelsLike = root.findViewById(R.id.tv_temperature_feels_like);
        tvSpeed = root.findViewById(R.id.tv_speed);
        tvAccuracy = root.findViewById(R.id.tv_accuracy);
        tvAltitude = root.findViewById(R.id.tv_altitude);
        ivStart = root.findViewById(R.id.iv_start);
        ivStop = root.findViewById(R.id.iv_stop);
        if (Constants.startTime != 0L) {
            tvJourneyStartedAt.setText(new SimpleDateFormat("KK:mm a", Locale.getDefault()).format(Constants.startTime));
        }
        setIconVisibility();

        ivStart.setOnClickListener(view -> {
            if(!Constants.IS_WATCH_CONNECTED) {
                Intent intent = new Intent(getActivity(), WatchScanActivity.class);
                startActivity(intent);
            }
            else {
                Constants.weatherResponse = null;
                enableLocationSettings();
                Constants.locationList = new ArrayList<>();
                Constants.startTime = System.currentTimeMillis();
                customHandler.post(updateTimeThread);
                Constants.IS_JOURNEY_STARTED = true;
                tvJourneyStartedAt.setText(new SimpleDateFormat("KK:mm a", Locale.getDefault()).format(Constants.startTime));
                setIconVisibility();
            }
        });
        ivStop.setOnClickListener(view -> {
            timeSwapBuff += timeInSeconds;
            Constants.IS_JOURNEY_STARTED = false;
            timeInSeconds = 0L;
            timeSwapBuff = 0L;
            updatedTime = 0L;
            setIconVisibility();
            if (isMyServiceRunning(BackgroundServices.class)) {
                isGPSServiceBound = false;
                getActivity().unbindService(GPSServiceConnection);
                getActivity().stopService(serviceIntent);
            }
            customHandler.removeCallbacks(updateTimeThread);
        });

    }

    private void setIconVisibility() {
        if (!Constants.IS_JOURNEY_STARTED) {
            ivStart.setVisibility(View.VISIBLE);
            ivStop.setVisibility(View.GONE);
        }
        else {
            if (isMyServiceRunning(BackgroundServices.class)) {
                getActivity().bindService(serviceIntent, GPSServiceConnection, Context.BIND_AUTO_CREATE | Context.BIND_IMPORTANT);
            }
            if (Constants.locationList.size()>=1) {
                Location currentLocation = Constants.locationList.get(Constants.locationList.size()-1);
                tvSpeed.setText(String.format(Locale.getDefault(),"%.2f", (currentLocation.getSpeed() * 3.6)) + " km/h");
                tvAccuracy.setText(String.format(Locale.getDefault(),"%.2f", (currentLocation.getAccuracy())) + "");
                tvAltitude.setText(String.format(Locale.getDefault(),"%.2f", (currentLocation.getAltitude())) + "");
            }

            if (Constants.weatherResponse != null) {
                tvWeather.setText(Constants.weatherResponse.getWeather().get(0).getMain());
                tvWindSpeed.setText(Constants.weatherResponse.getWind().getSpeed() + " km/h");
                tvHumidity.setText(Constants.weatherResponse.getMain().getHumidity() + "%");
                tvClouds.setText(Constants.weatherResponse.getClouds().getAll() + "%");
                if (Constants.weatherResponse.getVisibility() >= 1000) {
                    tvVisibility.setText((Constants.weatherResponse.getVisibility() / 1000) + " km");
                } else {
                    tvVisibility.setText((Constants.weatherResponse.getVisibility() / 1000) + " m");
                }
                tvTemperature.setText(Constants.weatherResponse.getMain().getTemp() + "??C");
                tvMinTemperature.setText(Constants.weatherResponse.getMain().getTempMin() + "??C");
                tvMaxTemperature.setText(Constants.weatherResponse.getMain().getTempMax() + "??C");
                tvTemperatureFeelsLike.setText(Constants.weatherResponse.getMain().getFeelsLike() + "??C");
                tvCountry.setText(Constants.weatherResponse.getName() + ", " + Constants.weatherResponse.getSys().getCountry());
                tvSunrise.setText(getDateString(Constants.weatherResponse.getSys().getSunrise()) + " am");
                tvSunset.setText(getDateString(Constants.weatherResponse.getSys().getSunset()) + " pm");
            }
            if (Constants.startTime != 0L) {
                tvJourneyStartedAt.setText(new SimpleDateFormat("KK:mm a", Locale.getDefault()).format(Constants.startTime));
            }
            ivStart.setVisibility(View.GONE);
            ivStop.setVisibility(View.VISIBLE);
        }
    }

    public void enableLocationSettings() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            LocationRequest locationRequest = LocationRequest.create()
                    .setInterval(0)
                    .setFastestInterval(0)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            LocationServices.getSettingsClient(getActivity()).checkLocationSettings(builder.build())
                    .addOnSuccessListener(getActivity(), (LocationSettingsResponse response) -> {

                    }).addOnFailureListener(getActivity(), ex -> {
                        if (ex instanceof ResolvableApiException) {
                            // Location settings are NOT satisfied,  but this can be fixed  by showing the user a dialog.
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) ex;
                                resolvable.startResolutionForResult(getActivity(), Constants.Location_SERVICE_REQUEST_CODE);
                            } catch (IntentSender.SendIntentException sendEx) {
                                // Ignore the error.
                            }
                        }
                    });
        } else {
            serviceIntent = new Intent(getActivity(), com.app.smartwatchapplication.Services.BackgroundServices.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d("SERVICE", "STARTING SERVICE");
                getActivity().startForegroundService(serviceIntent);
            } else {
                Log.d("SERVICE", "STARTING SERVICE");
                getActivity().startService(serviceIntent);
            }
            getActivity().bindService(serviceIntent, GPSServiceConnection, Context.BIND_AUTO_CREATE | Context.BIND_IMPORTANT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.Location_SERVICE_REQUEST_CODE && Activity.RESULT_OK == resultCode) {
            if (mapsFragment != null) {
                mapsFragment.getMapAsync(this);
            }
            Constants.locationList = new ArrayList<>();
            serviceIntent = new Intent(getActivity(), com.app.smartwatchapplication.Services.BackgroundServices.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d("SERVICE", "STARTING SERVICE");
                getActivity().startForegroundService(serviceIntent);
            } else {
                Log.d("SERVICE", "STARTING SERVICE");
                getActivity().startService(serviceIntent);
            }
            getActivity().bindService(serviceIntent, GPSServiceConnection, Context.BIND_AUTO_CREATE | Context.BIND_IMPORTANT);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("SimpleDateFormat")
    private String getDateString(long timeInMilliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(timeInMilliseconds);
    }
}