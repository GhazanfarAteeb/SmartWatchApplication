package com.app.smartwatchapplication.Activities.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.smartwatchapplication.Activities.ActivityMain;
import com.app.smartwatchapplication.AppConstants.Constants;
import com.app.smartwatchapplication.R;
import com.app.smartwatchapplication.SharedPreferences.SharedPref;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpVerificationActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Button btnVerifyOTP;
    String vID;
    OtpEditText otpEditText;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        init();
        setListeners();
        assert Constants.USER.getUser() != null;
        sendVerificationCode(Constants.USER.getUser().get(0).getvApiUsername());
    }

    private void init() {
        otpEditText = findViewById(R.id.et_otp);
        firebaseAuth = FirebaseAuth.getInstance();
        btnVerifyOTP = findViewById(R.id.btn_verify_otp);
        assert Constants.USER.getUser() != null;
        progressDialog = new ProgressDialog(OtpVerificationActivity.this);
        progressDialog.setMessage("Verifying OTP ...");
        progressDialog.setCancelable(false);
    }

    private void setListeners() {
        findViewById(R.id.tv_resend_otp).setOnClickListener(view -> {
            assert Constants.USER.getUser() != null;
            sendVerificationCode(Constants.USER.getUser().get(0).getvApiUsername());
        });
        btnVerifyOTP.setOnClickListener(view -> {
            if (!otpEditText.getText().toString().isEmpty() && otpEditText.getText().toString().length() == 6) {
                verifyCode(otpEditText.getText().toString());

            }
        });
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        SharedPref.writeBoolean(Constants.KEY_BOOLEAN_OTP_VERIFIED, true);
                        Intent i = new Intent(OtpVerificationActivity.this, ActivityMain.class);
                        startActivity(i);
                        finish();
                    } else {
                        task.getException().getLocalizedMessage();
                    }
                });
    }


    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(number)
                        .setTimeout(20L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                vID = s;
                            }
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                final String code = phoneAuthCredential.getSmsCode();
                                if (code != null) {
                                    otpEditText.setText(code);
                                    verifyCode(code);
                                }
                            }
                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                e.getLocalizedMessage();
                            }

                            @Override
                            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                                super.onCodeAutoRetrievalTimeOut(s);
                                System.out.println("TIMEOUT: "+s);
                                progressDialog.dismiss();
                                Toast.makeText(OtpVerificationActivity.this, "Unable to connect. Check your internet and try again", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyCode(String code) {
        try {
            progressDialog.show();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(vID, code);
            signInWithCredential(credential);
        } catch (Exception e) {
            System.out.println("WRONG CODE ENTERED");
            System.out.println(e.getMessage());
            progressDialog.dismiss();
        }
    }
    @Override
    public void onBackPressed () {
        moveTaskToBack(true);
    }
}