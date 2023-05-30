package com.halil.weatherapp.entity

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.halil.weatherapp.R
import com.halil.weatherapp.view.MainActivity

data class WeatherDetail(
    var type:Int,
    var icon:Drawable,
    var title:String,
    var text:String,
    var slideValue:Double,
    var otherData:HashMap<String,String>?=null,
    var otherText:String?=null

) {

}