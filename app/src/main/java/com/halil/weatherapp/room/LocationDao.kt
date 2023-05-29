package com.halil.weatherapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.halil.weatherapp.entity.DatabaseLocation
import com.halil.weatherapp.entity.Location

@Dao
interface LocationDao {

    @Insert(entity = DatabaseLocation::class)
    suspend fun insertAll(vararg location: DatabaseLocation):List<Long>

    @Query(value = "SELECT * FROM DatabaseLocation")
    suspend fun getAll():List<DatabaseLocation>

    @Query(value = "Delete from DatabaseLocation")
    suspend fun deleteAll()



}