package com.app.smartwatchapplication.Modals;

public class PostReadings {
    String id;
    String lat;
    String lon;
    String timestamp;
    String altitude;
    String speed;
    String bearing;
    String accuracy;
    String comment;
    String description;
    String temp;
    String feelsLike;
    String tempMin;
    String tempMax;
    String pressure;
    String humidity;
    String wind;
    String clouds;
    String visibility;
    String systolicBP;
    String diastolicBP;
    String heartRate;
    String spo2;
    String respirationRate;

    public PostReadings() {
    }

    public PostReadings(String id, String lat, String lon, String timestamp, String altitude, String speed, String bearing, String accuracy, String comment, String description, String temp, String feelsLike, String tempMin, String tempMax, String pressure, String humidity, String wind, String clouds, String visibility, String systolicBP, String diastolicBP, String heartRate, String spo2, String respirationRate) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.timestamp = timestamp;
        this.altitude = altitude;
        this.speed = speed;
        this.bearing = bearing;
        this.accuracy = accuracy;
        this.comment = comment;
        this.description = description;
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
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getBearing() {
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(String feelsLike) {
        this.feelsLike = feelsLike;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getClouds() {
        return clouds;
    }

    public void setClouds(String clouds) {
        this.clouds = clouds;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getSystolicBP() {
        return systolicBP;
    }

    public void setSystolicBP(String systolicBP) {
        this.systolicBP = systolicBP;
    }

    public String getDiastolicBP() {
        return diastolicBP;
    }

    public void setDiastolicBP(String diastolicBP) {
        this.diastolicBP = diastolicBP;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getSpo2() {
        return spo2;
    }

    public void setSpo2(String spo2) {
        this.spo2 = spo2;
    }

    public String getRespirationRate() {
        return respirationRate;
    }

    public void setRespirationRate(String respirationRate) {
        this.respirationRate = respirationRate;
    }
}
