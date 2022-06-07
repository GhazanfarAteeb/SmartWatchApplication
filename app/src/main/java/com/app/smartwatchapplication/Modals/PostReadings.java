package com.app.smartwatchapplication.Modals;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PostReadings {
    private @NonNull
    String id;
    private @NonNull
    String lat;
    private @NonNull
    String lon;
    private @NonNull
    String timestamp;
    private @Nullable
    String altitude;
    private @Nullable
    String speed;
    private @Nullable
    String bearing;
    private @Nullable
    String accuracy;
    private @Nullable
    String temp;
    private @Nullable
    String feelsLike;
    private @Nullable
    String tempMin;
    private @Nullable
    String tempMax;
    private @Nullable
    String pressure;
    private @Nullable
    String humidity;
    private @Nullable
    String wind;
    private @Nullable
    String clouds;
    private @Nullable
    String visibility;
    private @Nullable
    String systolicBP;
    private @Nullable
    String diastolicBP;
    private @Nullable
    String heartRate;
    private @Nullable
    String spo2;
    private @Nullable
    String respirationRate;
    int status;

    public PostReadings(
            @NonNull String id,
            @NonNull String lat,
            @NonNull String lon,
            @NonNull String timestamp,
            @Nullable String altitude,
            @Nullable String speed,
            @Nullable String bearing,
            @Nullable String accuracy,
            @Nullable String systolicBP,
            @Nullable String diastolicBP,
            @Nullable String heartRate,
            @Nullable String spo2,
            @Nullable String respirationRate,
            int status
    ) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.timestamp = timestamp;
        this.altitude = altitude;
        this.speed = speed;
        this.bearing = bearing;
        this.accuracy = accuracy;
        this.status = status;
        this.temp = null;
        this.feelsLike = null;
        this.tempMin = null;
        this.tempMax = null;
        this.pressure = null;
        this.humidity = null;
        this.wind = null;
        this.clouds = null;
        this.visibility = null;
        this.systolicBP = systolicBP;
        this.diastolicBP = diastolicBP;
        this.heartRate = heartRate;
        this.spo2 = spo2;
        this.respirationRate = respirationRate;
    }

    public PostReadings(
            @NonNull String id,
            @NonNull String lat,
            @NonNull String lon,
            @NonNull String timestamp,
            @Nullable String altitude,
            @Nullable String speed,
            @Nullable String bearing,
            @Nullable String accuracy,
            @Nullable String temp,
            @Nullable String feelsLike,
            @Nullable String tempMin,
            @Nullable String tempMax,
            @Nullable String pressure,
            @Nullable String humidity,
            @Nullable String wind,
            @Nullable String clouds,
            @Nullable String visibility,
            @Nullable String systolicBP,
            @Nullable String diastolicBP,
            @Nullable String heartRate,
            @Nullable String spo2,
            @Nullable String respirationRate,
            int status) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.timestamp = timestamp;
        this.altitude = altitude;
        this.speed = speed;
        this.bearing = bearing;
        this.accuracy = accuracy;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.pressure = pressure;
        this.humidity = humidity;
        this.wind = wind;
        this.clouds = clouds;
        this.visibility = visibility;
        this.systolicBP = systolicBP;
        this.diastolicBP = diastolicBP;
        this.heartRate = heartRate;
        this.spo2 = spo2;
        this.respirationRate = respirationRate;
        this.status = status;
    }


    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getLat() {
        return lat;
    }

    public void setLat(@NonNull String lat) {
        this.lat = lat;
    }

    @NonNull
    public String getLon() {
        return lon;
    }

    public void setLon(@NonNull String lon) {
        this.lon = lon;
    }

    @NonNull
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull String timestamp) {
        this.timestamp = timestamp;
    }

    @Nullable
    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(@Nullable String altitude) {
        this.altitude = altitude;
    }

    @Nullable
    public String getSpeed() {
        return speed;
    }

    public void setSpeed(@Nullable String speed) {
        this.speed = speed;
    }

    @Nullable
    public String getBearing() {
        return bearing;
    }

    public void setBearing(@Nullable String bearing) {
        this.bearing = bearing;
    }

    @Nullable
    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(@Nullable String accuracy) {
        this.accuracy = accuracy;
    }

    @Nullable
    public String getTemp() {
        return temp;
    }

    public void setTemp(@Nullable String temp) {
        this.temp = temp;
    }

    @Nullable
    public String getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(@Nullable String feelsLike) {
        this.feelsLike = feelsLike;
    }

    @Nullable
    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(@Nullable String tempMin) {
        this.tempMin = tempMin;
    }

    @Nullable
    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(@Nullable String tempMax) {
        this.tempMax = tempMax;
    }

    @Nullable
    public String getPressure() {
        return pressure;
    }

    public void setPressure(@Nullable String pressure) {
        this.pressure = pressure;
    }

    @Nullable
    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(@Nullable String humidity) {
        this.humidity = humidity;
    }

    @Nullable
    public String getWind() {
        return wind;
    }

    public void setWind(@Nullable String wind) {
        this.wind = wind;
    }

    @Nullable
    public String getClouds() {
        return clouds;
    }

    public void setClouds(@Nullable String clouds) {
        this.clouds = clouds;
    }

    @Nullable
    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(@Nullable String visibility) {
        this.visibility = visibility;
    }

    @Nullable
    public String getSystolicBP() {
        return systolicBP;
    }

    public void setSystolicBP(@Nullable String systolicBP) {
        this.systolicBP = systolicBP;
    }

    @Nullable
    public String getDiastolicBP() {
        return diastolicBP;
    }

    public void setDiastolicBP(@Nullable String diastolicBP) {
        this.diastolicBP = diastolicBP;
    }

    @Nullable
    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(@Nullable String heartRate) {
        this.heartRate = heartRate;
    }

    @Nullable
    public String getSpo2() {
        return spo2;
    }

    public void setSpo2(@Nullable String spo2) {
        this.spo2 = spo2;
    }

    @Nullable
    public String getRespirationRate() {
        return respirationRate;
    }

    public void setRespirationRate(@Nullable String respirationRate) {
        this.respirationRate = respirationRate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
