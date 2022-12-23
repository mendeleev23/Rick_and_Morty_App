package com.example.aston_courseproject_rickmorty.model.repository

import com.example.aston_courseproject_rickmorty.model.Episode
import com.example.aston_courseproject_rickmorty.model.Location
import com.example.aston_courseproject_rickmorty.model.database.EpisodeForListDb
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.model.database.LocationDb
import com.example.aston_courseproject_rickmorty.model.database.LocationForListDb
import com.example.aston_courseproject_rickmorty.model.dto.CharacterDto
import com.example.aston_courseproject_rickmorty.model.retrofit.ApiState
import com.example.aston_courseproject_rickmorty.model.retrofit.RetrofitServices
import com.example.aston_courseproject_rickmorty.utils.mapper.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CharacterDetailsRepository(
    private val mService: RetrofitServices,
    private val database: ItemsDatabase
) {

    suspend fun getCharacter(characterId: Int): Flow<ApiState<CharacterDto>> {
        return flow {
            val character = CharacterMapper(LocationForCharacterMapper()).transform(mService.getOneCharacter(characterId))
            emit(ApiState.success(character))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCharacterDb(characterId: Int): Flow<ApiState<CharacterDto>> {
        return flow {
            val array: Array<Int> = database.getCharacterEpisodeJoinDao().getEpisodesIdForCharacter(characterId)
            val character = CharacterDbMapper(LocationForCharacterMapper(), array).transform(database.getCharacterDao().getOneById(characterId))
            emit(ApiState.success(character))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getEpisodeList(episodesId: String): Flow<ApiState<MutableList<Episode>>> {
        return flow {
            val episodes = mService.getSeveralEpisodes(episodesId)
            emit(ApiState.success(episodes))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getEpisodeListDb(characterId: Int): Flow<ApiState<MutableList<EpisodeForListDb>>> {
        return flow {
            val episodesArray = database.getCharacterEpisodeJoinDao().getEpisodesIdForCharacter(characterId)
            val episodes = mutableListOf<EpisodeForListDb>()
            for (i in episodesArray.indices) {
                episodes.add(database.getEpisodeDao().getOneForListById(episodesArray[i]))
            }
            emit(ApiState.success(episodes))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getLocation(locationId: Int): Flow<ApiState<Location>> {
        return flow {
            val location = mService.getOneLocation(locationId)
            emit(ApiState.success(location))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getLocationDb(locationId: Int): Flow<ApiState<LocationForListDb>> {
        return flow {
            val location = database.getLocationDao().getOneForListById(locationId)
            emit(ApiState.success(location))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun saveEpisodesInDb(episodesList: MutableList<Episode>) {
        database.getEpisodeDao().insertAll(EpisodeToDbMapper().transform(episodesList))
        val listOfCharacterToEpisodes = EpisodeCharacterJoinMapper().transform(episodesList)
        database.getEpisodeCharacterJoinDao().insertAll(listOfCharacterToEpisodes)
    }

    suspend fun saveLocationInDb(location: Location) {
        database.getLocationDao().insertAll(LocationDb.locationToDb(mutableListOf(location)))
        val listOfCharacterToEpisodes = LocationCharacterJoinMapper().transform(mutableListOf(location))
        database.getLocationCharacterJoinDao().insertAll(listOfCharacterToEpisodes)
    }
}