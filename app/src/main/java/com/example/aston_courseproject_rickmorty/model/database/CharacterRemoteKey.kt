package com.example.aston_courseproject_rickmorty.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys_character")
data class CharacterRemoteKey(
    @PrimaryKey val characterId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
