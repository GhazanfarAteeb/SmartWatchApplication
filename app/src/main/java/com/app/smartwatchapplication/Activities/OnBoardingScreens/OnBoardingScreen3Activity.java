package com.app.smartwatchapplication.Activities.OnBoardingScreens;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.app.smartwatchapplication.Activities.Login.LoginActivity;
import com.app.smartwatchapplication.R;

public class OnBoardingScreen3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screen3);
        findViewById(R.id.iv_next).setOnClickListener(view -> {
            Intent intent = new Intent(OnBoardingScreen3Activity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}