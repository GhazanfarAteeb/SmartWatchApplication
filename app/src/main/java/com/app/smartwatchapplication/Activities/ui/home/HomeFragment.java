package com.app.smartwatchapplication.Activities.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.app.smartwatchapplication.Activities.WatchScanActivity;
import com.app.smartwatchapplication.AppConstants.Constants;
import com.app.smartwatchapplication.R;
import com.app.smartwatchapplication.databinding.FragmentHomeBinding;
import com.htsmart.wristband2.WristbandApplication;
import com.htsmart.wristband2.WristbandManager;

public class HomeFragment extends Fragment {
    View view;
    private FragmentHomeBinding binding;
    CardView cvConnectWatch;
    CardView cvConnectedWatch;
    TextView tvWatchName;
    TextView tvWatchMacAddress;
    ImageView ivDisconnectWatch;
    WristbandManager wristbandManager = WristbandApplication.getWristbandManager();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        initView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setWatch();
    }

    public void initView() {
        cvConnectedWatch = view.findViewById(R.id.cv_connected_watch);
        cvConnectWatch = view.findViewById(R.id.cv_connect_watch);
        tvWatchName = view.findViewById(R.id.tv_watch_name);
        tvWatchMacAddress = view.findViewById(R.id.tv_watch_mac_address);
        ivDisconnectWatch = view.findViewById(R.id.iv_unlink);
    }

    public void setWatch() {
        if (wristbandManager.isConnected()) {
            cvConnectedWatch.setVisibility(View.VISIBLE);
            cvConnectWatch.setVisibility(View.GONE);
            if (Constants.connectedDevice != null) {
                tvWatchName.setText(Constants.connectedDevice.getName());
                tvWatchMacAddress.setText(Constants.connectedDevice.getBluetoothDevice().getAddress());
            }
        } else {
            cvConnectedWatch.setVisibility(View.GONE);
            cvConnectWatch.setVisibility(View.VISIBLE);
        }
        ivDisconnectWatch.setOnClickListener(view1 -> {
            Constants.connectedDevice = null;
            cvConnectedWatch.setVisibility(View.GONE);
            cvConnectWatch.setVisibility(View.VISIBLE);
            wristbandManager.close();
            Constants.connectedDevice = null;
        });
        cvConnectWatch.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), WatchScanActivity.class)));


    }
}