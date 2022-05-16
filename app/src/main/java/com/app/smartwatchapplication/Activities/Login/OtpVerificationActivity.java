package com.app.smartwatchapplication.Activities.Login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.app.smartwatchapplication.Activities.WatchScanActivity;
import com.app.smartwatchapplication.R;

public class OtpVerificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        findViewById(R.id.btn_verify_otp).setOnClickListener(view -> {
            startActivity(new Intent(OtpVerificationActivity.this, WatchScanActivity.class));
        });
    }
}