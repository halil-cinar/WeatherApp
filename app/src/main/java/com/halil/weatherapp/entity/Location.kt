package com.halil.weatherapp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
 data class Location (

  @SerializedName("name"            ) @ColumnInfo(name = "name") var name           : String? = null,
  @SerializedName("region"          ) @ColumnInfo(name = "region")  var region         : String? = null,
  @SerializedName("country"         ) @ColumnInfo(name = "country")  var country        : String? = null,
  @SerializedName("lat"             ) @ColumnInfo(name = "lat")  var lat            : Double? = null,
  @SerializedName("lon"             ) @ColumnInfo(name = "lon")  var lon            : Double? = null,
  @SerializedName("tz_id"           ) @ColumnInfo(name = "tzId")  var tzId           : String? = null,
  @SerializedName("localtime_epoch" ) @ColumnInfo(name = "localtimeEpoch")  var localtimeEpoch : Int?    = null,
  @SerializedName("localtime"       ) @ColumnInfo(name = "localtime")  var localtime      : String? = null

){
  @PrimaryKey(autoGenerate = true)
  var uuid:Int=0
}