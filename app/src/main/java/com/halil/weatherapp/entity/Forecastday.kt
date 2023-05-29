package com.halil.weatherapp.entity

import com.google.gson.annotations.SerializedName


data class Forecastday (

  @SerializedName("date"       ) var date      : String?         = null,
  @SerializedName("date_epoch" ) var dateEpoch : Int?            = null,
  @SerializedName("day"        ) var day       : Day?            = null,
  @SerializedName("astro"      ) var astro     : Astro?          =null,
  @SerializedName("hour"       ) var hour      : ArrayList<Hour>,
  var dayName:String?=null

)