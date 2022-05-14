package com.app.smartwatchapplication.Utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.app.smartwatchapplication.AppConstants.Constants;

public class PermissionUtils {
    private static final String[] PERMISSIONS_FOR_ANDROID_S_AND_ABOVE = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final String[] PERMISSIONS = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static boolean checkPermission(AppCompatActivity activity, String permission) {
        return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void checkAndRequestPermissions(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            if (checkPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                    && checkPermission(activity, Manifest.permission.BLUETOOTH) && checkPermission(activity, Manifest.permission.BLUETOOTH_ADMIN)) {
                Constants.isFineLocationPermissionGranted = true;
                Constants.isCoarseLocationPermissionGranted = true;
                Constants.isBluetoothPermissionGranted = true;
                Constants.isBluetoothAdminPermissionGranted = true;
            } else {
                ActivityResultLauncher<String[]> permissionRequest = activity.registerForActivityResult(
                        new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            Boolean bluetoothPermissionGranted = result.getOrDefault(Manifest.permission.BLUETOOTH, false);
                            Boolean bluetoothAdminPermissionGranted = result.getOrDefault(Manifest.permission.BLUETOOTH_ADMIN, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                Log.d("LoginActivity", "Fine location permission granted");
                                Constants.isFineLocationPermissionGranted = true;
                            }
                            if (coarseLocationGranted != null && coarseLocationGranted) {
                                Log.d("LoginActivity", "Coarse location permission granted");
                                Constants.isCoarseLocationPermissionGranted = true;
                            }
                            if (bluetoothPermissionGranted != null && bluetoothPermissionGranted) {
                                Log.d("LoginActivity", "Bluetooth permission granted");
                                Constants.isBluetoothPermissionGranted = true;
                            }
                            if (bluetoothAdminPermissionGranted != null && bluetoothAdminPermissionGranted) {
                                Log.d("LoginActivity", "Bluetooth admin permission granted");
                                Constants.isBluetoothAdminPermissionGranted = true;
                            }
                        });
                permissionRequest.launch(PERMISSIONS);
            }
        } else {
            if (checkPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                    && checkPermission(activity, Manifest.permission.BLUETOOTH) && checkPermission(activity, Manifest.permission.BLUETOOTH_ADMIN)
                    && checkPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) && checkPermission(activity, Manifest.permission.BLUETOOTH_SCAN)) {
                Constants.isFineLocationPermissionGranted = true;
                Constants.isCoarseLocationPermissionGranted = true;
                Constants.isBluetoothPermissionGranted = true;
                Constants.isBluetoothAdminPermissionGranted = true;
                Constants.isBluetoothConnectPermissionGranted = true;
                Constants.isBluetoothScanPermissionGranted = true;
            } else {
                ActivityResultLauncher<String[]> permissionRequest = activity.registerForActivityResult(
                        new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            Boolean bluetoothPermissionGranted = result.getOrDefault(Manifest.permission.BLUETOOTH, false);
                            Boolean bluetoothAdminPermissionGranted = result.getOrDefault(Manifest.permission.BLUETOOTH_ADMIN, false);
                            Boolean bluetoothConnectGranted = result.getOrDefault(Manifest.permission.BLUETOOTH_CONNECT, false);
                            Boolean bluetoothScanGranted = result.getOrDefault(Manifest.permission.BLUETOOTH_SCAN, false);

                            if (fineLocationGranted != null && fineLocationGranted) {
                                Log.d("LoginActivity", "Fine location permission granted");
                                Constants.isFineLocationPermissionGranted = true;
                            }
                            if (coarseLocationGranted != null && coarseLocationGranted) {
                                Log.d("LoginActivity", "Coarse location permission granted");
                                Constants.isCoarseLocationPermissionGranted = true;
                            }
                            if (bluetoothPermissionGranted != null && bluetoothPermissionGranted) {
                                Log.d("LoginActivity", "Bluetooth permission granted");
                                Constants.isBluetoothPermissionGranted = true;
                            }
                            if (bluetoothAdminPermissionGranted != null && bluetoothAdminPermissionGranted) {
                                Log.d("LoginActivity", "Bluetooth admin permission granted");
                                Constants.isBluetoothAdminPermissionGranted = true;
                            }
                            if (bluetoothConnectGranted != null && bluetoothConnectGranted) {
                                Log.d("LoginActivity", "Bluetooth connect permission granted");
                                Constants.isBluetoothConnectPermissionGranted = true;
                            }
                            if (bluetoothScanGranted != null && bluetoothScanGranted) {
                                Log.d("LoginActivity", "Bluetooth scan permission granted");
                                Constants.isBluetoothScanPermissionGranted = true;
                            }
                        });
                permissionRequest.launch(PERMISSIONS_FOR_ANDROID_S_AND_ABOVE);
            }
        }
    }
}
