package com.example.aston_courseproject_rickmorty.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aston_courseproject_rickmorty.model.database.CharacterEpisodeJoin

@Dao
interface CharacterEpisodeJoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characterEpisodeJoin: MutableList<CharacterEpisodeJoin>)

    @Query("SELECT id FROM episodes_full_info INNER JOIN character_episode_join ON episodes_full_info.id=character_episode_join.episodeId WHERE character_episode_join.characterId=:characterId")
    fun getEpisodesIdForCharacter(characterId: Int): Array<Int>

    @Query("DELETE FROM character_episode_join")
    suspend fun deleteAll()
}