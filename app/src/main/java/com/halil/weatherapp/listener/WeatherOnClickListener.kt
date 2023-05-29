package com.halil.weatherapp.listener

import android.view.View
import com.halil.weatherapp.entity.Weather

interface WeatherOnClickListener {
    fun onClick(view: View,weather: Weather)
    fun onLongClick(view: View,weather: Weather):Boolean
}