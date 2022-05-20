package com.app.smartwatchapplication.Apis;

import com.app.smartwatchapplication.Modals.Weather.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("data/2.5/weather?")
    Call<Weather> getCurrentWeatherData(@Query("lat") String lat, @Query("lon") String lon, @Query("appid") String app_id, @Query("mode") String mode, @Query("units") String units);
}
