package com.example.aston_courseproject_rickmorty.model.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aston_courseproject_rickmorty.model.database.CharacterDb
import com.example.aston_courseproject_rickmorty.model.database.CharacterForListDb
import com.example.aston_courseproject_rickmorty.model.dto.CharacterForListDto
import com.example.aston_courseproject_rickmorty.model.dto.EpisodeForListDto

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterDb>)

    @Query("SELECT id, name, status, species, gender, image FROM characters_full_info")
    fun getAll(): List<CharacterForListDto>

    @Query("SELECT * FROM characters_full_info WHERE id = :characterId")
    suspend fun getOneById(characterId: Int): CharacterDb

    @Query("SELECT id, name, status, species, gender, image FROM characters_full_info WHERE id = :characterId")
    suspend fun getOneForListById(characterId: Int): CharacterForListDb

    @Query("DELETE FROM characters_full_info")
    suspend fun deleteAll()

    @Query("SELECT id, name, status, species, gender, image FROM characters_full_info WHERE (:name = '' OR name LIKE '%' || :name || '%') AND (:status = '' OR status = :status) AND (:species = '' OR species LIKE '%' || :species || '%') AND (:type = '' OR type LIKE '%' || :type || '%') AND (:gender = '' OR gender = :gender)")
    fun getSeveralForFilter(name: String, status: String, species: String, type: String, gender: String): PagingSource<Int, CharacterForListDto>

    @Query("SELECT id, name, status, species, gender, image FROM characters_full_info WHERE (:name = '' OR name LIKE '%' || :name || '%') AND (:status = '' OR status = :status) AND (:species = '' OR species LIKE '%' || :species || '%') AND (:type = '' OR type LIKE '%' || :type || '%') AND (:gender = '' OR gender = :gender)")
    fun getSeveralForFilterCheck(name: String, status: String, species: String, type: String, gender: String): List<CharacterForListDto>
}