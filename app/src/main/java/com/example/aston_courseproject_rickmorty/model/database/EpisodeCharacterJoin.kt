package com.example.aston_courseproject_rickmorty.model.database

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "episode_character_join",
    primaryKeys = [ "episodeId", "characterId" ],
    foreignKeys = [
        ForeignKey(
            entity = EpisodeDb::class,
            parentColumns = ["id"],
            childColumns = ["episodeId"]
        )
    ])
data class EpisodeCharacterJoin(
    val episodeId: Int,
    val characterId: Int
)