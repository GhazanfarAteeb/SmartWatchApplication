package com.app.smartwatchapplication.Activities.Login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.app.smartwatchapplication.Activities.ActivityMain;
import com.app.smartwatchapplication.R;
import com.app.smartwatchapplication.Utils.PermissionUtils;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        PermissionUtils.checkAndRequestPermissions(LoginActivity.this);
        findViewById(R.id.btn_login).setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, OtpVerificationActivity.class));
        });
    }

}