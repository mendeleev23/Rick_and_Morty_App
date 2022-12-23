package com.example.aston_courseproject_rickmorty.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aston_courseproject_rickmorty.model.database.EpisodeCharacterJoin

@Dao
interface EpisodeCharacterJoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodeCharacterJoin: MutableList<EpisodeCharacterJoin>)

    @Query("SELECT id FROM characters_full_info INNER JOIN episode_character_join ON characters_full_info.id=episode_character_join.characterId WHERE episode_character_join.episodeId=:episodeId")
    fun getCharactersIdForEpisode(episodeId: Int): Array<Int>

    @Query("DELETE FROM episode_character_join")
    suspend fun deleteAll()
}