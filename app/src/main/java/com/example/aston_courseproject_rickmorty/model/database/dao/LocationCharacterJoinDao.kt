package com.example.aston_courseproject_rickmorty.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aston_courseproject_rickmorty.model.database.LocationCharacterJoin

@Dao
interface LocationCharacterJoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(locationCharacterJoin: MutableList<LocationCharacterJoin>)

    @Query("SELECT id FROM characters_full_info INNER JOIN location_character_join ON characters_full_info.id=location_character_join.characterId WHERE location_character_join.locationId=:locationId")
    fun getCharactersIdForLocation(locationId: Int): Array<Int>

    @Query("DELETE FROM location_character_join")
    suspend fun deleteAll()
}