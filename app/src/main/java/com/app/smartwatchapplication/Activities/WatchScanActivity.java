package com.app.smartwatchapplication.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.app.smartwatchapplication.Modals.Watch;
import com.app.smartwatchapplication.R;
import com.htsmart.wristband2.WristbandApplication;
import com.htsmart.wristband2.WristbandManager;
import com.htsmart.wristband2.bean.ConnectionState;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.scan.ScanSettings;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

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
                        }
                    }
                }, Throwable::getLocalizedMessage);


    }
}