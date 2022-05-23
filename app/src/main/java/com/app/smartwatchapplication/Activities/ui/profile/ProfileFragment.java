package com.app.smartwatchapplication.Activities.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.smartwatchapplication.databinding.FragmentProfileBinding;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    TextInputEditText tieName, tieAddress, tiePhoneNumber, tieLicenseNumber,
            tieDateOfJoining, tieVehicleName, tieManufacturer, tieEngineNumber,
            tieChasisNumber, tieVehicleType;
    View view;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void init() {

    }
}