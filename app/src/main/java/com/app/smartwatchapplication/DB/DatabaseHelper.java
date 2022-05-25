package com.app.smartwatchapplication.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.app.smartwatchapplication.AppConstants.Constants;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String READINGS_TABLE_NAME = "READINGS";
    public static final String READINGS_ID = "ID";
    public static final String READINGS_LAT = "LAT";
    public static final String READINGS_LON = "LON";
    public static final String READINGS_TIMESTAMP = "TIMESTAMP";
    public static final String READINGS_ALTITUDE = "ALTITUDE";
    public static final String READINGS_ACCURACY = "ACCURACY";
    public static final String READINGS_SPEED = "SPEED";
    public static final String READINGS_BEARING = "BEARING";
    public static final String READINGS_TEMP = "TEMPERATURE";
    public static final String READINGS_FEELS_LIKE = "FEELS_LIKE";
    public static final String READINGS_TEMP_MAX = "MAXIMUM_TEMPERATURE";
    public static final String READINGS_TEMP_MIN = "MINIMUM_TEMPERATURE";
    public static final String READINGS_PRESSURE = "PRESSURE";
    public static final String READINGS_HUMIDITY = "HUMIDITY";
    public static final String READINGS_WIND = "WIND";
    public static final String READINGS_CLOUDS = "CLOUDS";
    public static final String READINGS_VISIBILITY = "VISIBILITY";
    public static final String READINGS_SYSTOLIC_BLOOD_PRESSURE = "SYSTOLIC_BLOOD_PRESSURE";
    public static final String READINGS_DIASTOLIC_BLOOD_PRESSURE = "DIASTOLIC_BLOOD_PRESSURE";
    public static final String READINGS_HEART_RATE = "HEART_RATE";
    public static final String READINGS_BLOOD_OXYGEN = "BLOOD_OXYGEN";
    public static final String READINGS_RESPIRATION_RATE = "RESPIRATION_RATE";
    public static final String READINGS_STATUS = "STATUS";

    public DatabaseHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + READINGS_TABLE_NAME + "(" +
                        READINGS_ID + " TEXT NOT NULL, " +
                        READINGS_LAT + " TEXT NOT NULL, " +
                        READINGS_LON + " TEXT NOT NULL, " +
                        READINGS_TIMESTAMP + " TEXT NOT NULL, " +
                        READINGS_ALTITUDE + " TEXT, " +
                        READINGS_ACCURACY + " TEXT, " +
                        READINGS_SPEED + " TEXT, " +
                        READINGS_BEARING + " TEXT, " +
                        READINGS_TEMP + " TEXT, " +
                        READINGS_FEELS_LIKE + " TEXT, " +
                        READINGS_TEMP_MIN + " TEXT, " +
                        READINGS_TEMP_MAX + " TEXT, " +
                        READINGS_PRESSURE + " TEXT, " +
                        READINGS_HUMIDITY + " TEXT, " +
                        READINGS_WIND + " TEXT, " +
                        READINGS_CLOUDS + " TEXT, " +
                        READINGS_VISIBILITY + " TEXT, " +
                        READINGS_SYSTOLIC_BLOOD_PRESSURE + " TEXT, " +
                        READINGS_DIASTOLIC_BLOOD_PRESSURE + " TEXT, " +
                        READINGS_HEART_RATE + " TEXT, " +
                        READINGS_BLOOD_OXYGEN + " TEXT, " +
                        READINGS_RESPIRATION_RATE + " TEXT, " +
                        READINGS_STATUS + " INTEGER NOT NULL" +
                        ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ READINGS_TABLE_NAME);
    }
}
