package com.example.aston_courseproject_rickmorty.model.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aston_courseproject_rickmorty.model.database.EpisodeDb
import com.example.aston_courseproject_rickmorty.model.database.EpisodeForListDb
import com.example.aston_courseproject_rickmorty.model.dto.EpisodeForListDto

@Dao
interface EpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodes: List<EpisodeDb>)

    @Query("SELECT id, name, air_date, episode FROM episodes_full_info")
    fun getAll(): List<EpisodeForListDto>

    @Query("SELECT * FROM episodes_full_info WHERE id = :episodeId")
    fun getOneById(episodeId: Int): EpisodeDb

    @Query("SELECT id, name, air_date, episode FROM episodes_full_info WHERE id = :episodeId")
    fun getOneForListById(episodeId: Int): EpisodeForListDb

    @Query("DELETE FROM episodes_full_info")
    suspend fun deleteAll()

    @Query("SELECT id, name, air_date, episode FROM episodes_full_info WHERE (:name = '' OR name LIKE '%' || :name || '%') AND (:episode = '' OR episode LIKE '%' || :episode || '%')")
    fun getSeveralForFilter(name: String, episode: String): PagingSource<Int, EpisodeForListDto>

    @Query("SELECT id, name, air_date, episode FROM episodes_full_info WHERE (:name = '' OR name LIKE '%' || :name || '%') AND (:episode = '' OR episode LIKE '%' || :episode || '%')")
    fun getSeveralForFilterCheck(name: String, episode: String): List<EpisodeForListDto>
}
