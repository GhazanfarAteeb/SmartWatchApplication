package com.app.smartwatchapplication.Activities.OnBoardingScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.app.smartwatchapplication.R;

public class OnBoardingScreen1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screen1);
        findViewById(R.id.iv_next).setOnClickListener(view -> {
            startActivity(new Intent(OnBoardingScreen1Activity.this, OnBoardingScreen2Activity.class));
            finish();
        });
    }
}