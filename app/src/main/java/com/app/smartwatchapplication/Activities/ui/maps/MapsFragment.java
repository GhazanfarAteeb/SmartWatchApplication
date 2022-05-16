package com.app.smartwatchapplication.Activities.ui.maps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.smartwatchapplication.R;
import com.app.smartwatchapplication.databinding.FragmentMapsBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private FragmentMapsBinding binding;
    public static GoogleMap map;
    View root;
    SupportMapFragment mapsFragment;
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
        map.getUiSettings().setAllGesturesEnabled(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mapsFragment !=null) {
            mapsFragment.getMapAsync(this);
        }
    }
}