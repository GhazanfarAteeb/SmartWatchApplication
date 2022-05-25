package com.app.smartwatchapplication.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.app.smartwatchapplication.AppConstants.Constants;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + Constants.READINGS_TABLE_NAME + "(" +
                        Constants.READINGS_ID + " TEXT NOT NULL, " +
                        Constants.READINGS_LAT + " TEXT NOT NULL, " +
                        Constants.READINGS_LON + " TEXT NOT NULL, " +
                        Constants.READINGS_TIMESTAMP + " TEXT NOT NULL, " +
                        Constants.READINGS_ALTITUDE + " TEXT, " +
                        Constants.READINGS_ACCURACY + " TEXT, " +
                        Constants.READINGS_SPEED + " TEXT, " +
                        Constants.READINGS_BEARING + " TEXT, " +
                        Constants.READINGS_TEMP + " TEXT, " +
                        Constants.READINGS_FEELS_LIKE + " TEXT, " +
                        Constants.READINGS_TEMP_MIN + " TEXT, " +
                        Constants.READINGS_TEMP_MAX + " TEXT, " +
                        Constants.READINGS_PRESSURE + " TEXT, " +
                        Constants.READINGS_HUMIDITY + " TEXT, " +
                        Constants.READINGS_WIND + " TEXT, " +
                        Constants.READINGS_CLOUDS + " TEXT, " +
                        Constants.READINGS_VISIBILITY + " TEXT, " +
                        Constants.READINGS_SYSTOLIC_BLOOD_PRESSURE + " TEXT, " +
                        Constants.READINGS_DIASTOLIC_BLOOD_PRESSURE + " TEXT, " +
                        Constants.READINGS_HEART_RATE + " TEXT, " +
                        Constants.READINGS_BLOOD_OXYGEN + " TEXT, " +
                        Constants.READINGS_RESPIRATION_RATE + " TEXT, " +
                        Constants.READINGS_STATUS + " INTEGER NOT NULL" +
                        ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ Constants.READINGS_TABLE_NAME);
    }
}
