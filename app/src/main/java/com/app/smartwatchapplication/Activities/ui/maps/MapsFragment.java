package com.app.smartwatchapplication.Activities.ui.maps;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.app.smartwatchapplication.AppConstants.Constants;
import com.app.smartwatchapplication.R;
import com.app.smartwatchapplication.Services.BackgroundServices;
import com.app.smartwatchapplication.databinding.FragmentMapsBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private FragmentMapsBinding binding;
    public static GoogleMap map;
    View root;
    SupportMapFragment mapsFragment;
    Intent serviceIntent;
    public static TextView tvJourneyStartedAt, tvJourneyTime, tvCountry, tvSunrise, tvSunset, tvBloodPressure, tvHeartRate,
            tvBloodOxygen, tvRespirationRate, tvWeather, tvWindSpeed, tvHumidity, tvClouds, tvVisibility, tvTemperature, tvMinTemperature, tvMaxTemperature,
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
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        tvSpeed = root.findViewById(R.id.tv_speed);
        tvAccuracy = root.findViewById(R.id.tv_accuracy);
        tvAltitude = root.findViewById(R.id.tv_altitude);
    }
}