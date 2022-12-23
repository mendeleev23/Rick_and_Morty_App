package com.example.aston_courseproject_rickmorty.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys_location")
class LocationRemoteKey(
    @PrimaryKey val locationId: String,
    val prevKey: Int?,
    val nextKey: Int?
)