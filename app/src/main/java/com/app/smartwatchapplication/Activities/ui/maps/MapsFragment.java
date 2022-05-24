package com.app.smartwatchapplication.Activities.ui.maps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.ArrayList;

@SuppressLint("StaticFieldLeak")
public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private FragmentMapsBinding binding;
    public static GoogleMap map;
    View root;
    SupportMapFragment mapsFragment;
    Intent serviceIntent;
    public static TextView tvJourneyStartedAt, tvJourneyTime, tvCountry, tvSunrise, tvSunset, tvBloodPressure, tvHeartRate,
            tvBloodOxygen, tvRespirationRate, tvWeather, tvWindSpeed, tvHumidity, tvClouds, tvVisibility, tvTemperature, tvMinTemperature, tvMaxTemperature, tvTemperatureFeelsLike,
    tvSpeed, tvAccuracy, tvAltitude;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        mapsFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        if(Constants.connectedDevice == null) {
            Intent intent = new Intent(getActivity(), WatchScanActivity.class);
            startActivity(intent);
        }
        else {
            enableLocationSettings();
        }
    }

    com.app.smartwatchapplication.Services.BackgroundServices locationServices;
    boolean isGPSServiceBound = false;
    private final ServiceConnection GPSServiceConnection = new ServiceConnection() {
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
                                resolvable.startResolutionForResult(getActivity(), 123);
                            } catch (IntentSender.SendIntentException sendEx) {
                                // Ignore the error.
                            }
                        }
                    });
        } else {
            if (mapsFragment != null) {
                mapsFragment.getMapAsync(this);
            }
            Constants.locationList = new ArrayList<>();
            serviceIntent = new Intent(getActivity(), com.app.smartwatchapplication.Services.BackgroundServices.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d("SERVICE", "STARTING SERVICE");
                getActivity().startForegroundService(new Intent(getActivity(), com.app.smartwatchapplication.Services.BackgroundServices.class));
            } else {
                Log.d("SERVICE", "STARTING SERVICE");
                getActivity().startService(new Intent(getActivity(), com.app.smartwatchapplication.Services.BackgroundServices.class));
            }
            getActivity().bindService(serviceIntent, GPSServiceConnection, Context.BIND_AUTO_CREATE | Context.BIND_IMPORTANT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && Activity.RESULT_OK == resultCode) {
            if (mapsFragment != null) {
                mapsFragment.getMapAsync(this);
            }
            Constants.locationList = new ArrayList<>();
            serviceIntent = new Intent(getActivity(), com.app.smartwatchapplication.Services.BackgroundServices.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d("SERVICE", "STARTING SERVICE");
                getActivity().startForegroundService(new Intent(getActivity(), com.app.smartwatchapplication.Services.BackgroundServices.class));
            } else {
                Log.d("SERVICE", "STARTING SERVICE");
                getActivity().startService(new Intent(getActivity(), com.app.smartwatchapplication.Services.BackgroundServices.class));
            }
            getActivity().bindService(serviceIntent, GPSServiceConnection, Context.BIND_AUTO_CREATE | Context.BIND_IMPORTANT);
        }
    }

}