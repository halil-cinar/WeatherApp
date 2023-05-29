package com.halil.weatherapp.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.halil.weatherapp.entity.Weather

class WeatherSharedPreferences {

    companion object{
        const val LatestWeatherKey="latestWeather"


        private var sharedPreferences:SharedPreferences?=null
        private var lock=Any()
        private var instance:WeatherSharedPreferences?=null

        private fun createPreferences(context: Context):WeatherSharedPreferences{
            sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context)
            return WeatherSharedPreferences()
        }

        operator fun invoke(context: Context)= instance?: synchronized(lock){
            instance?: createPreferences(context).also {
                instance=it
            }
        }
    }

    fun saveLatestWeather(src:Array<Weather>){
        sharedPreferences?.edit(commit = true){
            remove(LatestWeatherKey)
            putString(LatestWeatherKey,Gson().toJson(src))
        }
    }

    fun getLatestWeather(): Array<Weather>? {
        ( sharedPreferences?.getString(LatestWeatherKey,null))?.let {
            return Gson().fromJson<Array<Weather>>(it, Array<Weather>::class.java)
        }
        return null
    }



}