package com.example.weatherapp.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("v3/weather/weatherInfo")
    Call<WeatherResponse> getWeather(
            @Query("city") String cityAdcode,
            @Query("extensions") String extensions,
            @Query("key") String apiKey);}
