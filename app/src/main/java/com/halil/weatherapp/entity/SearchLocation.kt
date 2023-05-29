package com.halil.weatherapp.entity

import com.google.gson.annotations.SerializedName


data class SearchLocation (

    @SerializedName("id"      ) var id      : Int?    = null,
    @SerializedName("name"    ) var name    : String? = null,
    @SerializedName("region"  ) var region  : String? = null,
    @SerializedName("country" ) var country : String? = null,
    @SerializedName("lat"     ) var lat     : Double? = null,
    @SerializedName("lon"     ) var lon     : Double? = null,
    @SerializedName("url"     ) var url     : String? = null

)