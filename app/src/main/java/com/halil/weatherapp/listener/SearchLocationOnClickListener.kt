package com.halil.weatherapp.listener

import android.view.View
import com.halil.weatherapp.entity.SearchLocation

interface SearchLocationOnClickListener {

    fun add(view: View,searchLocation: SearchLocation?)

    fun onClick(view: View,searchLocation: SearchLocation?)

}