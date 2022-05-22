package com.app.smartwatchapplication.Activities.Login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.app.smartwatchapplication.Apis.Api;
import com.app.smartwatchapplication.AppConstants.Constants;
import com.app.smartwatchapplication.Modals.GoSafeLoginApiResponse;
import com.app.smartwatchapplication.R;
import com.app.smartwatchapplication.Utils.PermissionUtils;
import com.hbb20.CountryCodePicker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    EditText etPhoneNo;
    CountryCodePicker countryCodePicker;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
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
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Verifying ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            if(countryCodePicker.isValidFullNumber()) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.GO_SAFE_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Api service = retrofit.create(Api.class);
                Call call = service.postContact(countryCodePicker.getFullNumberWithPlus());
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) {
                        progressDialog.dismiss();
                        if(response.code() ==200) {
                            Constants.USER = (GoSafeLoginApiResponse) response.body();
                            assert Constants.USER != null;
                            if(Constants.USER.error) {
                                etPhoneNo.setError("Phone number not valid");
                            }
                            else {
                                Constants.USER_ID = Constants.USER.getUser().get(0).getvApiUsername();
                                startActivity(new Intent(LoginActivity.this, OtpVerificationActivity.class));
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull Throwable t) {

                    }
                });
            }
            else {
                etPhoneNo.setError("Phone number not valid");
                progressDialog.dismiss();
            }
        });
    }
    private void init() {
        countryCodePicker = findViewById(R.id.ccp_picker);
        etPhoneNo = findViewById(R.id.et_phone_no);
        countryCodePicker.registerCarrierNumberEditText(etPhoneNo);
    }
}