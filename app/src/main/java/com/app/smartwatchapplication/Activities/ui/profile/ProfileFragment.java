package com.app.smartwatchapplication.Activities.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.smartwatchapplication.databinding.FragmentProfileBinding;
import com.htsmart.wristband2.WristbandApplication;
import com.htsmart.wristband2.WristbandManager;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    WristbandManager wristbandManager = WristbandApplication.getWristbandManager();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}