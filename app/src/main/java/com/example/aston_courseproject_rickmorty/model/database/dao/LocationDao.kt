package com.example.aston_courseproject_rickmorty.model.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aston_courseproject_rickmorty.model.database.LocationDb
import com.example.aston_courseproject_rickmorty.model.database.LocationForListDb
import com.example.aston_courseproject_rickmorty.model.dto.LocationForListDto

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(locations: List<LocationDb>)

    @Query("SELECT id, name, type, dimension FROM locations_full_info")
    fun getAll(): List<LocationForListDto>

    @Query("SELECT * FROM locations_full_info WHERE id = :locationId")
    fun getOneById(locationId: Int): LocationDb

    @Query("SELECT id, name, type, dimension FROM locations_full_info WHERE id = :locationId")
    fun getOneForListById(locationId: Int): LocationForListDb

    @Query("DELETE FROM locations_full_info")
    suspend fun deleteAll()

    @Query("SELECT id, name, type, dimension FROM locations_full_info WHERE (:name = '' OR name LIKE '%' || :name || '%') AND (:type = '' OR type LIKE '%' || :type || '%') AND (:dimension = '' OR dimension LIKE '%' || :dimension || '%')")
    fun getSeveralForFilter(name: String, type: String, dimension: String): PagingSource<Int, LocationForListDto>

    @Query("SELECT id, name, type, dimension FROM locations_full_info WHERE (:name = '' OR name LIKE '%' || :name || '%') AND (:type = '' OR type LIKE '%' || :type || '%') AND (:dimension = '' OR dimension LIKE '%' || :dimension || '%')")
    fun getSeveralForFilterCheck(name: String, type: String, dimension: String): List<LocationForListDto>
}