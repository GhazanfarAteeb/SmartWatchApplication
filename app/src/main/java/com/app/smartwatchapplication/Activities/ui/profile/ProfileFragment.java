package com.app.smartwatchapplication.Activities.ui.profile;

import static com.app.smartwatchapplication.Activities.ui.maps.MapsFragment.GPSServiceConnection;
import static com.app.smartwatchapplication.Activities.ui.maps.MapsFragment.serviceIntent;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.smartwatchapplication.Activities.Login.LoginActivity;
import com.app.smartwatchapplication.Activities.ui.maps.MapsFragment;
import com.app.smartwatchapplication.AppConstants.Constants;
import com.app.smartwatchapplication.Modals.WatchReadings;
import com.app.smartwatchapplication.R;
import com.app.smartwatchapplication.Services.BackgroundServices;
import com.app.smartwatchapplication.SharedPreferences.SharedPref;
import com.app.smartwatchapplication.databinding.FragmentProfileBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.htsmart.wristband2.WristbandApplication;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    TextInputEditText tieName, tieAddress, tiePhoneNumber, tieLicenseNumber,
            tieDateOfJoining, tieVehicleName, tieManufacturer, tieEngineNumber,
            tieChassisNumber, tieVehicleType;
    Button btnLogout;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        setData();
        setListeners();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void init() {
        tieName = view.findViewById(R.id.tie_name);
        tieAddress = view.findViewById(R.id.tie_address);
        tiePhoneNumber = view.findViewById(R.id.tie_phone_no);
        tieEngineNumber = view.findViewById(R.id.tie_engine_no);
        tieDateOfJoining = view.findViewById(R.id.tie_doj);
        tieChassisNumber = view.findViewById(R.id.tie_chassis_no);
        tieLicenseNumber = view.findViewById(R.id.tie_license_no);
        tieManufacturer = view.findViewById(R.id.tie_manufacturer_name);
        tieVehicleName = view.findViewById(R.id.tie_vehicle_name);
        tieVehicleType = view.findViewById(R.id.tie_vehicle_type);
        btnLogout = view.findViewById(R.id.btn_logout);
    }

    private void setListeners() {
        btnLogout.setOnClickListener(view1 -> {
            Constants.latLngArrayList = new ArrayList<>();
            Constants.USER_ID = null;
            Constants.city = null;
            Constants.connectedDevice = null;
            Constants.weatherResponse = null;
            Constants.USER = null;
            Constants.locationList = null;
            WristbandApplication.getWristbandManager().close();
            Constants.currentWatchReadings = new WatchReadings();
            SharedPref.writeString(Constants.LOGIN_SAVED, null);
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
            FirebaseAuth.getInstance().signOut();
            SharedPref.writeBoolean(Constants.KEY_BOOLEAN_OTP_VERIFIED, false);
            SharedPref.writeBoolean(Constants.KEY_BOOLEAN_LOGIN_SAVED, false);
            Constants.IS_WATCH_CONNECTED = false;
            MapsFragment.customHandler.removeCallbacksAndMessages(null);
            if (MapsFragment.tvJourneyStartedAt != null) {
                MapsFragment.tvJourneyStartedAt.setText("-");
            }
            Constants.IS_JOURNEY_STARTED = false;
            if (isMyServiceRunning(BackgroundServices.class)) {
                getActivity().unbindService(GPSServiceConnection);
                getActivity().stopService(serviceIntent);
            }
        });
    }

    private void setData() {
        if (Constants.USER.getUser()!=null) {
            tieName.setText(Constants.USER.getUser().get(0).getdName());
            tieAddress.setText(Constants.USER.getUser().get(0).getdAddress());
            tiePhoneNumber.setText(Constants.USER.getUser().get(0).getdMobile());
            tieLicenseNumber.setText(Constants.USER.getUser().get(0).getdLicenseno());
            tieManufacturer.setText(Constants.USER.getUser().get(0).getvManufacturedBy());
            tieVehicleType.setText(Constants.USER.getUser().get(0).getvType());
            tieVehicleName.setText(Constants.USER.getUser().get(0).getvName());
            tieDateOfJoining.setText(Constants.USER.getUser().get(0).getdDoj());
            tieEngineNumber.setText(Constants.USER.getUser().get(0).getvEngineNo());
            tieChassisNumber.setText(Constants.USER.getUser().get(0).getvChassisNo());
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
}