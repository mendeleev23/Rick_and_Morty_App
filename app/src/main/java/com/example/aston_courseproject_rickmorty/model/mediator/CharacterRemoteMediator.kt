package com.example.aston_courseproject_rickmorty.model.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.aston_courseproject_rickmorty.model.database.CharacterRemoteKey
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.model.dto.CharacterForListDto
import com.example.aston_courseproject_rickmorty.model.retrofit.RetrofitServices
import com.example.aston_courseproject_rickmorty.utils.mapper.CharacterEpisodeJoinMapper
import com.example.aston_courseproject_rickmorty.utils.mapper.CharacterToDbMapper
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException


@ExperimentalPagingApi
class CharacterRemoteMediator(
    private val mServices: RetrofitServices,
    private val db: ItemsDatabase,
    private val filterQueryList: MutableList<String>
) : RemoteMediator<Int, CharacterForListDto>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterForListDto>
    ): MediatorResult {
        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        var isEmptyDb = false
        var isEmptyQuery = false
        Thread {
            val listEmpty = db.getCharacterDao().getAll()
            if (listEmpty.isEmpty()) {
                isEmptyDb = true
            }
            val listQuery = db.getCharacterDao().getSeveralForFilterCheck(filterQueryList[0], filterQueryList[1], filterQueryList[2], filterQueryList[3], filterQueryList[4])
            if (listQuery.isEmpty()) {
                isEmptyQuery = true
            }
        }.start()

        try {
            delay(100)
            val response = mServices.getSeveralCharactersFilter(page,filterQueryList[0], filterQueryList[1], filterQueryList[2], filterQueryList[3], filterQueryList[4])
            val isEndOfList = response.info.next == null
            val queryList = filterQueryList[0] + filterQueryList[1] + filterQueryList[2] + filterQueryList[3] + filterQueryList[4]
            db.withTransaction {
                if (loadType == LoadType.REFRESH && queryList == "") {
                    db.getCharacterEpisodeJoinDao().deleteAll()
                    db.getCharacterDao().deleteAll()
                    db.getCharacterKeysDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.results.map {
                    CharacterRemoteKey(
                        it.id.toString(),
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                db.getCharacterKeysDao().insertAll(keys)
                db.getCharacterDao().insertAll(CharacterToDbMapper().transform(response.results))
                val listOfCharacterToEpisodes = CharacterEpisodeJoinMapper().transform(response.results)
                db.getCharacterEpisodeJoinDao().insertAll(listOfCharacterToEpisodes)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (e: IOException) {
            if (isEmptyDb) {
                val error = IOException("Empty Database")
                return MediatorResult.Error(error)
            }
            if (isEmptyQuery) {
                val error = IOException("Wrong Query")
                return MediatorResult.Error(error)
            }
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            if (isEmptyQuery) {
                val error = IOException("Wrong Query")
                return MediatorResult.Error(error)
            }
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, CharacterForListDto>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
                prevKey
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, CharacterForListDto>): CharacterRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                db.getCharacterKeysDao().remoteKeysCharacterId(repoId.toString())
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, CharacterForListDto>): CharacterRemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { character ->
                db.getCharacterKeysDao().remoteKeysCharacterId(character.id.toString())
            }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, CharacterForListDto>): CharacterRemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { character ->
                db.getCharacterKeysDao().remoteKeysCharacterId(character.id.toString())
            }
    }

}