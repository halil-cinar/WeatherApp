package com.halil.weatherapp.listener

import android.view.View
import com.halil.weatherapp.entity.WeatherDetail

interface WeatherDetailOnClickListener {
    fun onClick(view: View,weatherDetail: WeatherDetail)
}