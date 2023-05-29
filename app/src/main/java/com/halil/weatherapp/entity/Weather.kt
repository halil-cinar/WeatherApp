package com.halil.weatherapp.entity

import com.google.gson.annotations.SerializedName


data class Weather (

  @SerializedName("location" ) var location : Location? = null,
  @SerializedName("current"  ) var current  : Current?  = null,
  @SerializedName("forecast" ) var forecast : Forecast? = null,
  @SerializedName("error" ) var error : Error? = null
)