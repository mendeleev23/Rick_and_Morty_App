package com.example.aston_courseproject_rickmorty.model.database

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "character_episode_join",
    primaryKeys = [ "characterId", "episodeId" ],
    foreignKeys = [
        ForeignKey(
            entity = CharacterDb::class,
            parentColumns = ["id"],
            childColumns = ["characterId"]
        )
    ])
data class CharacterEpisodeJoin(
    val characterId: Int,
    val episodeId: Int
)
