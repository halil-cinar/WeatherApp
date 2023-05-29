package com.halil.weatherapp.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherApiServis {

    companion object {
        private val baseUrl = "http://api.weatherapi.com/v1/"

        private var api = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)

        public suspend fun getForecastWeatherApi(
            location: String,
            days: String = "3",
            aqi: String = "no",
            alerts: String = "no",
        ) = api.getForecastWeatherApi(
            location,
            aqi,
            days,
            alerts
        ).body()

        public suspend fun getCurrentWeatherApi(
            location: String,
            days: String = "3",
            aqi: String = "no",
            alerts: String = "no",
        ) = api.getCurrentWeatherApi(
            location, aqi, days, alerts
        ).body()

        public suspend fun getSearchLocation(location: String) =
            api.getSearchLocation(location).body()

    }


}