package com.app.smartwatchapplication.Activities.Login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.app.smartwatchapplication.R;
import com.app.smartwatchapplication.Utils.PermissionUtils;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        PermissionUtils.checkAndRequestPermissions(LoginActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog builder = new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Function limited")
                        .setMessage("For better experience you need to allow location usage to all the time")
                        .setPositiveButton("OK", (dialog, which) -> {
                            ActivityCompat.requestPermissions(LoginActivity.this, new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1);
                        }).setNegativeButton("Cancel", (dialog, which) -> dialog.cancel()).create();
                builder.show();
            }
        }
        findViewById(R.id.btn_login).setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, OtpVerificationActivity.class));
        });
    }

}