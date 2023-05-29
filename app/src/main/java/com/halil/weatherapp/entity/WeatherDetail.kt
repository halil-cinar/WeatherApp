package com.halil.weatherapp.entity

import android.graphics.drawable.Drawable
import java.io.StringReader

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