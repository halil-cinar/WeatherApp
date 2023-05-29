package com.halil.weatherapp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseLocation(
    @ColumnInfo var name:String

){
    @PrimaryKey(autoGenerate = true)
    var id:Long=0
}
