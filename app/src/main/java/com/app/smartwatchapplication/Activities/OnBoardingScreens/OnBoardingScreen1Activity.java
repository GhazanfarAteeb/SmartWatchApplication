package com.app.smartwatchapplication.Activities.OnBoardingScreens;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.app.smartwatchapplication.AppConstants.Constants;
import com.app.smartwatchapplication.R;
import com.app.smartwatchapplication.SharedPreferences.SharedPref;

public class OnBoardingScreen1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screen1);
        SharedPref.writeBoolean(Constants.ON_BOARDING_SHOWN, true);
        findViewById(R.id.iv_next).setOnClickListener(view -> {
            startActivity(new Intent(OnBoardingScreen1Activity.this, OnBoardingScreen2Activity.class));
            finish();
        });
    }
}