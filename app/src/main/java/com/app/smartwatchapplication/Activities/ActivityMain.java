package com.app.smartwatchapplication.Activities;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.app.smartwatchapplication.Apis.Api;
import com.app.smartwatchapplication.AppConstants.Constants;
import com.app.smartwatchapplication.DB.DatabaseHelper;
import com.app.smartwatchapplication.Listeners.DbThread;
import com.app.smartwatchapplication.Modals.PostReadings;
import com.app.smartwatchapplication.Modals.PostReadingsResponse;
import com.app.smartwatchapplication.R;
import com.app.smartwatchapplication.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//MAIN ACTIVITY WHICH CONTAINS FRAGMENTS
public class ActivityMain extends AppCompatActivity implements DbThread.DBThreadListener {
    DatabaseHelper dbHelper = new DatabaseHelper(ActivityMain.this);
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
        sendDataToDB();
    }

    private void sendDataToDB() {
        //TO CHECK THE IF INTERNET IS AVAILABLE THEN THE SQLITE DATABASE READINGS WILL BE SENT TO THE SERVER THROUGH APIs
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //IF MY NETWORK IS AVAILABLE
        if (networkInfo != null && networkInfo.isConnected()) {
            // READ THE SQLITE DATABASE
            Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                    "SELECT * FROM " + Constants.READINGS_TABLE_NAME + " WHERE "
                            + Constants.READINGS_STATUS + "=?",
                    new String[]{
                            String.valueOf(Constants.STATUS_NOT_UPLOADED)
                    }
            );

            // IF TABLE SEQUENCES ARE CHANGED THEN THIS WILL GET THE INDEX ACCORDING TO THE CHANGED INDEXES
            int locationReadingsID = cursor.getColumnIndex(Constants.READINGS_ID);
            int locationReadingsLat = cursor.getColumnIndex(Constants.READINGS_LAT);
            int locationReadingsLon = cursor.getColumnIndex(Constants.READINGS_LON);
            int locationReadingsTimestamp = cursor.getColumnIndex(Constants.READINGS_TIMESTAMP);
            int locationReadingsAltitude = cursor.getColumnIndex(Constants.READINGS_ALTITUDE);
            int locationReadingsSpeed = cursor.getColumnIndex(Constants.READINGS_SPEED);
            int locationReadingsBearing = cursor.getColumnIndex(Constants.READINGS_BEARING);
            int locationReadingsAccuracy = cursor.getColumnIndex(Constants.READINGS_ACCURACY);
            int locationReadingsTemp = cursor.getColumnIndex(Constants.READINGS_TEMP);
            int locationReadingsFeelsLike = cursor.getColumnIndex(Constants.READINGS_FEELS_LIKE);
            int locationReadingsTempMax = cursor.getColumnIndex(Constants.READINGS_TEMP_MAX);
            int locationReadingsTempMin = cursor.getColumnIndex(Constants.READINGS_TEMP_MIN);
            int locationReadingsPressure = cursor.getColumnIndex(Constants.READINGS_PRESSURE);
            int locationReadingsHumidity = cursor.getColumnIndex(Constants.READINGS_HUMIDITY);
            int locationReadingsWind = cursor.getColumnIndex(Constants.READINGS_WIND);
            int locationReadingsClouds = cursor.getColumnIndex(Constants.READINGS_CLOUDS);
            int locationReadingsVisibility = cursor.getColumnIndex(Constants.READINGS_VISIBILITY);
            int locationReadingsSystolicBP = cursor.getColumnIndex(Constants.READINGS_SYSTOLIC_BLOOD_PRESSURE);
            int locationReadingsDiastolicBP = cursor.getColumnIndex(Constants.READINGS_DIASTOLIC_BLOOD_PRESSURE);
            int locationReadingsHeartRate = cursor.getColumnIndex(Constants.READINGS_HEART_RATE);
            int locationReadingsSPO2 = cursor.getColumnIndex(Constants.READINGS_BLOOD_OXYGEN);
            int locationReadingsRespirationRate = cursor.getColumnIndex(Constants.READINGS_RESPIRATION_RATE);
            int locationReadingsStatus = cursor.getColumnIndex(Constants.READINGS_STATUS);
            ArrayList<PostReadings> postReadingsArrayList = new ArrayList<>();

            // GETTING DATA FROM THE CURSOR
            while (cursor.moveToNext()) {
                postReadingsArrayList.add(
                        new PostReadings(
                                cursor.getString(locationReadingsID),
                                cursor.getString(locationReadingsLat),
                                cursor.getString(locationReadingsLon),
                                cursor.getString(locationReadingsTimestamp),
                                cursor.getString(locationReadingsAltitude),
                                cursor.getString(locationReadingsSpeed),
                                cursor.getString(locationReadingsBearing),
                                cursor.getString(locationReadingsAccuracy),
                                cursor.getString(locationReadingsTemp),
                                cursor.getString(locationReadingsFeelsLike),
                                cursor.getString(locationReadingsTempMin),
                                cursor.getString(locationReadingsTempMax),
                                cursor.getString(locationReadingsPressure),
                                cursor.getString(locationReadingsHumidity),
                                cursor.getString(locationReadingsWind),
                                cursor.getString(locationReadingsClouds),
                                cursor.getString(locationReadingsVisibility),
                                cursor.getString(locationReadingsSystolicBP),
                                cursor.getString(locationReadingsDiastolicBP),
                                cursor.getString(locationReadingsHeartRate),
                                cursor.getString(locationReadingsSPO2),
                                cursor.getString(locationReadingsRespirationRate),
                                cursor.getInt(locationReadingsStatus)
                        )
                );
            }
            cursor.close();
            new DbThread(ActivityMain.this, postReadingsArrayList, dbHelper).start();
        }
    }

    @Override
    public void updateDB(List<PostReadings> postReadingsList, DatabaseHelper dbHelper) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.GO_SAFE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Api service = retrofit.create(Api.class);
            for (PostReadings reading : postReadingsList) {
                Call<PostReadingsResponse> readingsCall = service.postAllData(
                        reading.getId(),
                        reading.getLat(),
                        reading.getLon(),
                        reading.getTimestamp(),
                        reading.getAltitude(),
                        reading.getSpeed(),
                        reading.getBearing(),
                        reading.getAccuracy(),
                        reading.getTemp(),
                        reading.getFeelsLike(),
                        reading.getTempMin(),
                        reading.getTempMax(),
                        reading.getPressure(),
                        reading.getHumidity(),
                        reading.getWind(),
                        reading.getClouds(),
                        reading.getVisibility(),
                        reading.getSystolicBP(),
                        reading.getDiastolicBP(),
                        reading.getHeartRate(),
                        reading.getSpo2(),
                        reading.getRespirationRate()
                );

                Response<PostReadingsResponse> response = readingsCall.execute();
                System.out.println("POST_READINGS_RESPONSE" + response.code());
                if (response.code() == 200) {
                    PostReadingsResponse postReadingsResponse = response.body();
                    assert postReadingsResponse != null;
                    System.out.println(postReadingsResponse.success);
                    if (postReadingsList.size() > 0) {
                        dbHelper.getWritableDatabase().execSQL(
                                "UPDATE " + Constants.READINGS_TABLE_NAME + " SET " + Constants.READINGS_STATUS + "=" + Constants.STATUS_UPLOADED +
                                        " WHERE " +
                                        Constants.READINGS_ID + "='" + reading.getId() + "' AND " +
                                        Constants.READINGS_TIMESTAMP + "='" + reading.getTimestamp() + "'");
                    }
                }
            }
        } catch (Exception ex) {
            ex.getLocalizedMessage();
        }
    }
}