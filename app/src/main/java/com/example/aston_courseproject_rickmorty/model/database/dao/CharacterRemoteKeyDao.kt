package com.example.aston_courseproject_rickmorty.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aston_courseproject_rickmorty.model.database.CharacterRemoteKey

@Dao
interface CharacterRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<CharacterRemoteKey>)

    @Query("SELECT * FROM remote_keys_character WHERE characterId = :id")
    suspend fun remoteKeysCharacterId(id: String): CharacterRemoteKey?

    @Query("DELETE FROM remote_keys_character")
    suspend fun deleteAll()
}