package com.app.smartwatchapplication.Services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.app.smartwatchapplication.Activities.ActivityMain;
import com.app.smartwatchapplication.Activities.ui.maps.MapsFragment;
import com.app.smartwatchapplication.AppConstants.Constants;
import com.app.smartwatchapplication.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class BackgroundServices extends Service {
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager notificationManager;
    private PowerManager.WakeLock wakeLock;
    private NotificationCompat.Builder builder;
    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            MapsFragment.map.clear();
            Constants.watchReadingsList = new ArrayList<>();
            Location currentLocation = locationResult.getLastLocation();
            float speedInKMPH = (float) (currentLocation.getSpeed()*3.6);
            Log.d("CURRENT_SPEED", String.valueOf(speedInKMPH));

            List<LatLng> latLngArrayList = new ArrayList<>();
            for (Location loc : Constants.locationList) {
                latLngArrayList.add(new LatLng(loc.getLatitude(), loc.getLongitude()));
            }
//            MarkerOptions markerOptions = new MarkerOptions()
//                    .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
//                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(getApplicationContext(), R.drawable.ic_maps_arrow)));

            if (Math.round(speedInKMPH) != 0) {
                Constants.locationList.add(currentLocation);
            }
            MapsFragment.map.addPolyline(new PolylineOptions().addAll(latLngArrayList).width(5).color(Color.BLUE).geodesic(true));
            MapsFragment.tvSpeed.setText(String.format("%.2f", (currentLocation.getSpeed()*3.6))+" km/h");
            MapsFragment.tvAccuracy.setText(String.format("%.2f", (currentLocation.getAccuracy()))+"");
            MapsFragment.tvAltitude.setText(String.format("%.2f", (currentLocation.getAltitude()))+"");
            Log.d(null, "================ USER DETAILS ================");
            Log.d("CURRENT_LOCATION : ", currentLocation.getLatitude() + "," + currentLocation.getLongitude());
            Log.d("CURRENT_SPEED : ", String.valueOf(currentLocation.getSpeed()));
            Log.d("CURRENT_ALTITUDE : ", String.valueOf(currentLocation.getAltitude()));
            Log.d("CURRENT_ACCURACY : ", String.valueOf(currentLocation.getAccuracy()));
            Log.d(null, "==============================================");
//            MapsFragment.map.addMarker(markerOptions);
            MapsFragment.map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 17.f));
        }
    };

    public static void createLocationRequest() {
        Constants.locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(Constants.INTERVAL)
                .setFastestInterval(Constants.FASTEST_INTERVAL);
    }

    public class LocalBinder extends Binder {
        public BackgroundServices getServiceInstance() {
            return BackgroundServices.this;
        }
    }
    private final IBinder locationServiceBinder = new LocalBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (wakeLock != null && !wakeLock.isHeld()) {
            wakeLock.acquire();
        }
        return locationServiceBinder;
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Constants.client.requestLocationUpdates(Constants.locationRequest,
                this.locationCallback, Looper.getMainLooper());
    }

    @Override
    public void onDestroy() {
        Constants.client.removeLocationUpdates(locationCallback);
        Log.d("STOP_SERVICE", "TRYING TO STOP SERVICE FROM ON DESTROY");
        stopSelf();
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Constants.client.removeLocationUpdates(locationCallback);
        stopSelf();
        Log.d("STOP_SERVICE", "TRYING TO STOP SERVICE");
        super.onTaskRemoved(rootIntent);
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        assert drawable != null;
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Constants.client = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(getApplicationContext());
        createLocationRequest();
        startLocationUpdates();
        Constants.notification = getNotification();
        startForeground(NOTIFICATION_ID, Constants.notification);
        Log.d("SERVICE", "ON START COMMAND");

        return START_NOT_STICKY;
    }

    private Notification getNotification() {
        final String CHANNEL_ID = "serviceChannel";

        builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        //builder.setSmallIcon(R.drawable.ic_notification_24dp)
        builder.setSmallIcon(R.mipmap.ic_launcher_round)
                .setColor(getResources().getColor(R.color.teal_200))
                .setContentTitle(getString(R.string.app_name))
                .setShowWhen(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        final Intent startIntent = new Intent(getApplicationContext(), ActivityMain.class);
        startIntent.setAction(Intent.ACTION_MAIN);
        startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 1, startIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(contentIntent);
        return builder.build();
    }
}
