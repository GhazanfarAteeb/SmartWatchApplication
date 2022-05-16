package com.app.smartwatchapplication.Activities.OnBoardingScreens;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.app.smartwatchapplication.R;

public class OnBoardingScreen2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screen2);
        findViewById(R.id.iv_next).setOnClickListener(view -> {
            startActivity(new Intent(OnBoardingScreen2Activity.this, OnBoardingScreen3Activity.class));
            finish();
        });
    }
}