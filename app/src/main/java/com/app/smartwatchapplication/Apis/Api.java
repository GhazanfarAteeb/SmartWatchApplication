package com.app.smartwatchapplication.Apis;

import com.app.smartwatchapplication.Modals.City;
import com.app.smartwatchapplication.Modals.GoSafeLoginApiResponse;
import com.app.smartwatchapplication.Modals.Weather.Weather;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {
    @GET("geo/1.0/reverse?")
    Call<List<City>> getCityData(@Query("lat") String lat, @Query("lon") String lon, @Query("appid") String app_id, @Query("limit") String limit);
    @GET("data/2.5/weather?")
    Call<Weather> getCurrentWeatherData(@Query("lat") String lat, @Query("lon") String lon, @Query("appid") String app_id, @Query("mode") String mode, @Query("units") String units);
    @FormUrlEncoded
    @POST("api/checkphon")
    Call<GoSafeLoginApiResponse> postContact(@Field("phone_no") String contactNo);
}
