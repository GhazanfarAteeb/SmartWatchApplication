package com.app.smartwatchapplication.Activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.app.smartwatchapplication.Modals.Watch;
import com.app.smartwatchapplication.R;
import com.htsmart.wristband2.WristbandApplication;
import com.htsmart.wristband2.WristbandManager;
import com.htsmart.wristband2.bean.ConnectionState;
import com.htsmart.wristband2.bean.HealthyDataResult;
import com.htsmart.wristband2.bean.WristbandConfig;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanSettings;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class WatchScanActivity extends AppCompatActivity {
    WristbandManager wristbandManager = WristbandApplication.getWristbandManager();
    Disposable scanDisposable;
    RxBleClient rxBleClient;
    //    BluetoothDevice btDevice;
    private Disposable stateDisposable;
    private Disposable errorDisposable;
    private ConnectionState connectionState = ConnectionState.DISCONNECTED;
    RxBleDevice device = null;
    List<Watch> watchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_scan);

        ScanSettings scanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .build();
        invalidateOptionsMenu();
        rxBleClient = WristbandApplication.getRxBleClient();
        scanDisposable = rxBleClient.scanBleDevices(scanSettings)
                .subscribe(scanResult -> {
                    Watch watch = new Watch(scanResult.getBleDevice().getName(), scanResult.getBleDevice().getMacAddress());
                    watchList.add(watch);

                    //TODO - MOVE IT TO WATCH ADAPTER
                    for (int i = 0; i < watchList.size(); i++) {
                        if (watchList.get(i).getWatchMacAddress().equals(scanResult.getBleDevice().getMacAddress())) {
                            watchList.set(i, watch);
                        }
                    }

                    //TODO - ADD ON CLICK LISTENER FOR WATCH SCANNING
                    if (watch.getWatchMacAddress().equals("00:E0:12:3F:94:81")) {
                        if (!wristbandManager.isConnected()) {
                            scanDisposable.dispose();
                            wristbandManager.connect(scanResult.getBleDevice(), "1", true, true, 30, (float) 165.5, (float) 70.8);
                            System.out.println("Connected to" + scanResult.getBleDevice().getMacAddress());
                            System.out.println("CONNECTION_STATE: " + wristbandManager.isConnected());
                            wristbandManager.observerConnectionState().subscribe(new Observer<ConnectionState>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    System.out.println(d.isDisposed());
                                }

                                @Override
                                public void onNext(ConnectionState connectionState) {
                                    if (connectionState.toString().equals("CONNECTED")) {
                                        WristbandConfig wristbandConfig = wristbandManager.getWristbandConfig();
                                        int healthType = 0;
                                        System.out.println(wristbandManager.isConnected());
                                        healthType |= WristbandManager.HEALTHY_TYPE_HEART_RATE;
                                        healthType |= WristbandManager.HEALTHY_TYPE_BLOOD_PRESSURE;
                                        healthType |= WristbandManager.HEALTHY_TYPE_OXYGEN;
                                        healthType |= WristbandManager.HEALTHY_TYPE_RESPIRATORY_RATE;
                                        Disposable healthSystem = wristbandManager.openHealthyRealTimeData(healthType, Integer.MAX_VALUE).
                                                observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Consumer<HealthyDataResult>() {
                                                    @Override
                                                    public void accept(HealthyDataResult healthyDataResult) throws Exception {
                                                        Log.d("HEART_RATE", String.valueOf(healthyDataResult.getHeartRate()));
                                                        Log.d("BP_RATE", "" + healthyDataResult.getSystolicPressure() + "/" + healthyDataResult.getDiastolicPressure());
                                                        Log.d("SPO2_RATE", String.valueOf(healthyDataResult.getOxygen()));
                                                        Log.d("RESPIRATION_RATE", String.valueOf(healthyDataResult.getRespiratoryRate()));
                                                    }
                                                }, new Consumer<Throwable>() {
                                                    @Override
                                                    public void accept(Throwable throwable) throws Exception {
                                                        throwable.getLocalizedMessage();
                                                    }
                                                });
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
                        }
                    }
                }, Throwable::getLocalizedMessage);


    }
}