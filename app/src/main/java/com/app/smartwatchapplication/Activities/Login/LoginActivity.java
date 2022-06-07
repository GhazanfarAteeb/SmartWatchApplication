package com.app.smartwatchapplication.Activities.Login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.app.smartwatchapplication.Activities.ActivityMain;
import com.app.smartwatchapplication.Apis.Api;
import com.app.smartwatchapplication.AppConstants.Constants;
import com.app.smartwatchapplication.Modals.GoSafeLoginApiResponse;
import com.app.smartwatchapplication.R;
import com.app.smartwatchapplication.SharedPreferences.SharedPref;
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
    String savedLogin;
    private boolean isLoginSaved;

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
                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1);
                        }).setNegativeButton("Cancel", (dialog, which) -> dialog.cancel()).create();
                builder.show();
            }
        }
        isLoginSaved = SharedPref.readBoolean(Constants.KEY_BOOLEAN_LOGIN_SAVED, false);
        if (isLoginSaved) {
            savedLogin = SharedPref.readString(Constants.LOGIN_SAVED, null);
            doLogin(savedLogin);
        }
        findViewById(R.id.btn_login).setOnClickListener(view -> {
            progressDialog.show();
            if (countryCodePicker.isValidFullNumber()) {
                doLogin(countryCodePicker.getFullNumberWithPlus());
            } else {
                etPhoneNo.setError("Phone number not valid");
                progressDialog.dismiss();
            }
        });
    }

    private void init() {
        if (SharedPref.readBoolean(Constants.ON_BOARDING_SHOWN, false)) {
            SharedPref.writeBoolean(Constants.ON_BOARDING_SHOWN, true);
        }
        countryCodePicker = findViewById(R.id.ccp_picker);
        etPhoneNo = findViewById(R.id.et_phone_no);
        countryCodePicker.registerCarrierNumberEditText(etPhoneNo);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Verifying ...");
        progressDialog.setCancelable(false);
    }
    private void doLogin(String number) {
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.GO_SAFE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api service = retrofit.create(Api.class);
        Call call = service.postContact(number);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    Constants.USER = (GoSafeLoginApiResponse) response.body();
                    assert Constants.USER != null;
                    if (Constants.USER.error) {
                        etPhoneNo.setError("Phone number not valid");
                    } else {
                        Constants.USER_ID = Constants.USER.getUser().get(0).getvApiUsername();
                        CheckBox cbRememberMe = findViewById(R.id.cb_remember_me);
                        if (cbRememberMe.isChecked()) {
                            SharedPref.writeBoolean(Constants.KEY_BOOLEAN_LOGIN_SAVED, true);
                            SharedPref.writeString(Constants.LOGIN_SAVED, countryCodePicker.getFullNumberWithPlus());
                        }
                        if (isLoginSaved) {
                            startActivity(new Intent(LoginActivity.this, ActivityMain.class));
                        }
                        else {
                            startActivity(new Intent(LoginActivity.this, OtpVerificationActivity.class));
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Unable to connect. Check your internet and try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed () {
        moveTaskToBack(true);
    }
}