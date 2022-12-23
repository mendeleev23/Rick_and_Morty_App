package com.example.aston_courseproject_rickmorty.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters_full_info")
data class CharacterDb(
    @PrimaryKey val id: Int? = null,
    val name: String? = null,
    val status: String? = null,
    val species: String? = null,
    val type: String? = null,
    val gender: String? = null,
    val origin_name: String? = null,
    val origin_url: String? = null,
    val location_name: String? = null,
    val location_url: String? = null,
    val image: String? = null,
    val url: String? = null,
    val created: String? = null
)

data class CharacterForListDb(
    val id: Int? = null,
    val name: String? = null,
    val status: String? = null,
    val species: String? = null,
    val gender: String? = null,
    val image: String? = null
)