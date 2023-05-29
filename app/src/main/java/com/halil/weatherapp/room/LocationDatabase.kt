package com.halil.weatherapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.halil.weatherapp.entity.DatabaseLocation
import com.halil.weatherapp.entity.Location

@Database(entities = arrayOf(DatabaseLocation::class), version = 2)
abstract class LocationDatabase:RoomDatabase() {

abstract fun getDao():LocationDao

companion object{
private fun createDatabase(context: Context): LocationDatabase {
 return Room.databaseBuilder(context.applicationContext,LocationDatabase::class.java,"location").build()
}

@Volatile  var instance :LocationDatabase?=null
 var lock=Any()
operator fun invoke(context: Context): LocationDatabase {
return instance?: synchronized(lock){
instance?: createDatabase(context).also {
instance=it
}
}
}
}

}