package com.app.smartwatchapplication.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smartwatchapplication.Activities.ui.maps.MapsFragment;
import com.app.smartwatchapplication.Adapters.WatchAdapter;
import com.app.smartwatchapplication.AppConstants.Constants;
import com.app.smartwatchapplication.R;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.htsmart.wristband2.WristbandApplication;
import com.htsmart.wristband2.WristbandManager;
import com.htsmart.wristband2.bean.ConnectionError;
import com.htsmart.wristband2.bean.ConnectionState;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.scan.ScanSettings;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class WatchScanActivity extends AppCompatActivity {
    WristbandManager wristbandManager = WristbandApplication.getWristbandManager();
    public static Disposable scanDisposable;
    RxBleClient rxBleClient;
    RecyclerView rvWatch;
    WatchAdapter watchAdapter;
    ScanSettings scanSettings;
    ProgressDialog progressDialog;
    ImageView ivBack;
    ImageView ivStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_scan);
        init();
        setListeners();
        setAdapters();

        if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            enableLocationSettings();
        } else {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ActivityResultLauncher<Intent> enableBluetoothActivityResult = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (Activity.RESULT_OK == result.getResultCode()) {
                            enableLocationSettings();
                        }
                    });
            enableBluetoothActivityResult.launch(enableBluetoothIntent);
        }
    }


    public void init() {
        rvWatch = findViewById(R.id.rv_watch);
        ivBack = findViewById(R.id.iv_back);
        ivStop = findViewById(R.id.iv_stop);
        progressDialog = new ProgressDialog(WatchScanActivity.this);
        AlertDialog builder = new AlertDialog.Builder(WatchScanActivity.this)
                .setMessage("In order to connect watch first you need to stop scan from the above red button.")
                .setCancelable(false).setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                }).create();
        builder.show();
    }

    private void setListeners() {
        ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
        ivStop.setOnClickListener(view -> {
            if (scanDisposable != null) {
                scanDisposable.dispose();
                Toast.makeText(WatchScanActivity.this, "Scan Stopped", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setAdapters() {
        watchAdapter = new WatchAdapter(WatchScanActivity.this);
        rvWatch.setAdapter(watchAdapter);
        rvWatch.setLayoutManager(new LinearLayoutManager(WatchScanActivity.this));
    }


    public void scanWatch() {
        rxBleClient = WristbandApplication.getRxBleClient();
        scanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .build();
        scanDisposable = rxBleClient.scanBleDevices(scanSettings)
                .subscribe(scanResult -> {
                    watchAdapter.setWatchConnectionListener(watch -> {
                        progressDialog.setMessage("Connecting ...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        if (!wristbandManager.isConnected()) {
                            wristbandManager.connect(watch, "1", true, true, 30, (float) 165, (float) 70.8);
                            Constants.connectedDevice = watch;
                        }
                    });
                    watchAdapter.addScanResult(scanResult);
                }, throwable -> {
                    throwable.printStackTrace();
                    Log.d("ERROR", throwable.getMessage());
                });
        wristbandManager.observerConnectionState().subscribe(new Observer<ConnectionState>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("DISPOSABLE" + d.isDisposed());
            }

            @Override
            public void onNext(ConnectionState connectionState) {
                if (connectionState.toString().equals("CONNECTED")) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();

                        AlertDialog builder = new AlertDialog.Builder(WatchScanActivity.this)
                                .setTitle("Watch Connected")
                                .setMessage("Your watch have been connected successfully.")
                                .setCancelable(false).setPositiveButton("OK", (dialog, which) -> {
                                    Intent intent = new Intent(WatchScanActivity.this, ActivityMain.class);
                                    startActivity(intent);
                                    finish();
                                    dialog.dismiss();
                                }).create();
                        builder.show();
                    }
                    int healthType = 0;
                    healthType |= WristbandManager.HEALTHY_TYPE_HEART_RATE;
                    healthType |= WristbandManager.HEALTHY_TYPE_BLOOD_PRESSURE;
                    healthType |= WristbandManager.HEALTHY_TYPE_OXYGEN;
                    healthType |= WristbandManager.HEALTHY_TYPE_RESPIRATORY_RATE;
                    Disposable HealthSystem = wristbandManager.openHealthyRealTimeData(healthType, Integer.MAX_VALUE).
                            observeOn(AndroidSchedulers.mainThread())
                            .subscribe(healthyDataResult -> {

                                Constants.currentWatchReadings.setSystolicBloodPressure(healthyDataResult.getSystolicPressure());
                                Constants.currentWatchReadings.setDiastolicBloodPressure(healthyDataResult.getDiastolicPressure());
                                Constants.currentWatchReadings.setHeartRate(healthyDataResult.getHeartRate());
                                Constants.currentWatchReadings.setBloodOxygenLevel(healthyDataResult.getOxygen());
                                Constants.currentWatchReadings.setRespirationRate(healthyDataResult.getRespiratoryRate());


                                if (MapsFragment.tvBloodPressure != null) {
                                    MapsFragment.tvBloodPressure.setText(Constants.currentWatchReadings.getSystolicBloodPressure() + "/" + Constants.currentWatchReadings.getDiastolicBloodPressure() + " mmHg");
                                }
                                if (MapsFragment.tvBloodOxygen != null) {
                                    MapsFragment.tvBloodOxygen.setText(Constants.currentWatchReadings.getBloodOxygenLevel() + "%");
                                }
                                if (MapsFragment.tvHeartRate != null) {
                                    MapsFragment.tvHeartRate.setText(Constants.currentWatchReadings.getHeartRate() + " BPM");
                                }
                                if (MapsFragment.tvRespirationRate != null) {
                                    MapsFragment.tvRespirationRate.setText(Constants.currentWatchReadings.getRespirationRate() + " per min.");
                                }
                                Log.d("HEART_RATE", String.valueOf(healthyDataResult.getHeartRate()));
                                Log.d("BP_RATE", "" + healthyDataResult.getSystolicPressure() + "/" + healthyDataResult.getDiastolicPressure());
                                Log.d("SPO2_RATE", String.valueOf(healthyDataResult.getOxygen()));
                                Log.d("RESPIRATION_RATE", String.valueOf(healthyDataResult.getRespiratoryRate()));
                            }, Throwable::getLocalizedMessage);
                }

            }

            @Override
            public void onError(Throwable e) {
                Log.d("ERROR", e.getMessage());
                System.out.println("CONNECTION FAILED");
            }

            @Override
            public void onComplete() {
                System.out.println("CONNECTED SUCCESSFULLY");
            }
        });
        Disposable errorDisposable = wristbandManager.observerConnectionError().subscribe(new Consumer<ConnectionError>() {
            @Override
            public void accept(ConnectionError connectionError) throws Exception {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    AlertDialog builder = new AlertDialog.Builder(WatchScanActivity.this)
                            .setTitle("Connection Error")
                            .setMessage("Unable to connect to watch. Please try again.")
                            .setCancelable(true).setPositiveButton("OK", (dialog, which) -> {
                                dialog.dismiss();
                            }).create();
                    builder.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (scanDisposable != null) {
            scanDisposable.dispose();
        }
        finishActivity(1);
    }

    public void enableLocationSettings() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            LocationRequest locationRequest = LocationRequest.create()
                    .setInterval(0)
                    .setFastestInterval(0)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
                    .addOnSuccessListener(this, (LocationSettingsResponse response) -> {
                        scanWatch();
                    }).addOnFailureListener(this, ex -> {
                        if (ex instanceof ResolvableApiException) {
                            // Location settings are NOT satisfied,  but this can be fixed  by showing the user a dialog.
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) ex;
                                resolvable.startResolutionForResult(WatchScanActivity.this, Constants.Location_SERVICE_REQUEST_CODE);
                            } catch (IntentSender.SendIntentException sendEx) {
                                // Ignore the error.
                            }
                        }
                    });
        } else {
            scanWatch();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==  Constants.Location_SERVICE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                scanWatch();
            }
        }
    }
}