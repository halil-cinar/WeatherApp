package com.halil.weatherapp.entity

import com.google.gson.annotations.SerializedName


data class Error (

    @SerializedName("code"    ) var code    : Int?    = null,
    @SerializedName("message" ) var message : String? = null

)
