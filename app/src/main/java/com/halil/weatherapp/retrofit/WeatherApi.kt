package com.halil.weatherapp.retrofit

import com.halil.weatherapp.entity.SearchLocation
import com.halil.weatherapp.entity.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    //Base url
    //http://api.weatherapi.com/v1/
    @GET("forecast.json?key=D81315FD80604F04A6F212332231005")
    suspend fun getForecastWeatherApi(
       @Query("q") location: String,
       @Query("aqi") aqi: String,
       @Query("days") days: String,
       @Query("alerts") alerts: String,
       //@Query("lang") lang:String="tr"
    ): Response<Weather>

    @GET("current.json?key=D81315FD80604F04A6F212332231005")
    suspend fun getCurrentWeatherApi(
       @Query("q") location: String,
       @Query("aqi") aqi: String,
       @Query("days") days: String,
       @Query("alerts") alerts: String,
    ): Response<Weather>

    @GET("search.json?key=d81315fd80604f04a6f212332231005")
    suspend fun getSearchLocation(
       @Query("q") location: String,
    ): Response<List<SearchLocation>>
}