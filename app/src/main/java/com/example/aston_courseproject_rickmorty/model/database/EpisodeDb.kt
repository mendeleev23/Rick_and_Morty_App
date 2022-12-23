package com.example.aston_courseproject_rickmorty.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "episodes_full_info")
data class EpisodeDb(
    @PrimaryKey val id: Int? = null,
    val name: String? = null,
    val air_date: String? = null,
    val episode: String? = null,
    val url: String? = null,
    val created: String? = null
)

data class EpisodeForListDb(
    val id: Int? = null,
    val name: String? = null,
    val air_date: String? = null,
    val episode: String? = null,
)