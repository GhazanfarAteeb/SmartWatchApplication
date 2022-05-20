package com.app.smartwatchapplication.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smartwatchapplication.Activities.ui.maps.MapsFragment;
import com.app.smartwatchapplication.Adapters.WatchAdapter;
import com.app.smartwatchapplication.AppConstants.Constants;
import com.app.smartwatchapplication.R;
import com.htsmart.wristband2.WristbandApplication;
import com.htsmart.wristband2.WristbandManager;
import com.htsmart.wristband2.bean.ConnectionState;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.scan.ScanSettings;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

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
        scanWatch();
    }


    public void init() {
        rvWatch = findViewById(R.id.rv_watch);
        ivBack = findViewById(R.id.iv_back);
        ivStop = findViewById(R.id.iv_stop);
        progressDialog = new ProgressDialog(WatchScanActivity.this);
    }

    private void setListeners() {
        ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
        ivStop.setOnClickListener(view -> {
            if(scanDisposable!= null) {
                scanDisposable.dispose();
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
                        progressDialog.setTitle("Connecting ...");
                        progressDialog.show();
                        if (!wristbandManager.isConnected()) {
                            wristbandManager.connect(watch, "1", false, true, 30, (float) 165, (float) 70.8);
                            System.out.println("WATCH CONNECTED: WITH MANAGER");
                            Constants.connectedDevice = watch;
                        }
                    });
                    watchAdapter.addScanResult(scanResult);
                    wristbandManager.observerConnectionState().subscribe(new Observer<ConnectionState>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            System.out.println("DISPOSABLE" + d.isDisposed());
                        }


                        @Override
                        public void onNext(ConnectionState connectionState) {
                            if (connectionState.toString().equals("CONNECTED")) {
                                if(progressDialog.isShowing()) {
                                    progressDialog.dismiss();

                                    AlertDialog builder = new AlertDialog.Builder(WatchScanActivity.this)
                                            .setMessage("Watch Connected successfully")
                                            .setTitle("Watch Connection")
                                            .setCancelable(false).setNeutralButton("OK", (dialog, which) -> {
                                                Intent intent = new Intent(WatchScanActivity.this, ActivityMain.class);
                                                dialog.dismiss();
                                                startActivity(intent);
                                            }).create();
                                    builder.show();
                                }
                                int healthType = 0;
                                System.out.println(wristbandManager.isConnected());
                                healthType |= WristbandManager.HEALTHY_TYPE_HEART_RATE;
                                healthType |= WristbandManager.HEALTHY_TYPE_BLOOD_PRESSURE;
                                healthType |= WristbandManager.HEALTHY_TYPE_OXYGEN;
                                healthType |= WristbandManager.HEALTHY_TYPE_RESPIRATORY_RATE;
                                Disposable HealthSystem = wristbandManager.openHealthyRealTimeData(healthType, Integer.MAX_VALUE).
                                        observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(healthyDataResult -> {
                                            if (Constants.watchReadingsList!= null) {
                                                Constants.currentWatchReadings.setSystolicBloodPressure(healthyDataResult.getSystolicPressure());
                                                Constants.currentWatchReadings.setDiastolicBloodPressure(healthyDataResult.getDiastolicPressure());
                                                Constants.currentWatchReadings.setHeartRate(healthyDataResult.getHeartRate());
                                                Constants.currentWatchReadings.setBloodOxygenLevel(healthyDataResult.getOxygen());
                                                Constants.currentWatchReadings.setRespirationRate(healthyDataResult.getRespiratoryRate());
                                                Log.d("ADDING_READINGS", "ADDING_READGINS");
                                            }
                                            if (MapsFragment.tvBloodPressure!= null) {
                                                MapsFragment.tvBloodPressure.setText(Constants.currentWatchReadings.getSystolicBloodPressure()+"/"+Constants.currentWatchReadings.getDiastolicBloodPressure());
                                            }
                                            if (MapsFragment.tvBloodOxygen != null) {
                                                MapsFragment.tvBloodOxygen.setText(Constants.currentWatchReadings.getBloodOxygenLevel()+"%");
                                            }
                                            if (MapsFragment.tvHeartRate != null) {
                                                MapsFragment.tvHeartRate.setText(Constants.currentWatchReadings.getHeartRate()+" BPM");
                                            }
                                            if (MapsFragment.tvRespirationRate != null) {
                                                MapsFragment.tvRespirationRate.setText(Constants.currentWatchReadings.getRespirationRate() +" per min.");
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
                            e.getLocalizedMessage();
                        }

                        @Override
                        public void onComplete() {
                            System.out.println("CONNECTED SUCCESSFULLY");
                        }
                    });
                }, Throwable::getLocalizedMessage);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(WatchScanActivity.this, ActivityMain.class));
    }
}