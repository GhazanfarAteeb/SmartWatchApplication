package com.app.smartwatchapplication.Apis;

import com.app.smartwatchapplication.Modals.City;
import com.app.smartwatchapplication.Modals.GoSafeLoginApiResponse;
import com.app.smartwatchapplication.Modals.PostReadingsResponse;
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
    Call<List<City>> getCityData(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("appid") String app_id,
            @Query("limit") String limit
    );

    @GET("data/2.5/weather?")
    Call<Weather> getCurrentWeatherData(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("appid") String app_id,
            @Query("mode") String mode,
            @Query("units") String units
    );

    @FormUrlEncoded
    @POST("api/checkphon")
    Call<GoSafeLoginApiResponse> postContact(
            @Field("phone_no") String contactNo
    );

    @FormUrlEncoded
    @POST("api")
    Call<PostReadingsResponse> postAllData(
            @Field("id") String id,
            @Field("lat") String lat,
            @Field("lon") String lon,
            @Field("timestamp") String timestamp,
            @Field("altitude") String altitude,
            @Field("speed") String speed,
            @Field("bearing") String bearing,
            @Field("accuracy") String accuracy,
            @Field("temp") String temp,
            @Field("feels_like") String feelsLike,
            @Field("temp_min") String tempMin,
            @Field("temp_max") String tempMax,
            @Field("pressure") String pressure,
            @Field("humidity") String humidity,
            @Field("wind") String wind,
            @Field("clouds") String clouds,
            @Field("visibility") String visibility,
            @Field("systolic_bp") String systolicBP,
            @Field("diastolic_bp") String diastolicBP,
            @Field("heart_rate") String heartRate,
            @Field("spo2") String spo2,
            @Field("respiration_rate") String respirationRate
    );
}
