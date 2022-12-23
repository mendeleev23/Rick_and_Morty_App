package com.example.aston_courseproject_rickmorty.model.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.aston_courseproject_rickmorty.model.database.EpisodeRemoteKey
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.model.dto.EpisodeForListDto
import com.example.aston_courseproject_rickmorty.model.retrofit.RetrofitServices
import com.example.aston_courseproject_rickmorty.utils.mapper.EpisodeCharacterJoinMapper
import com.example.aston_courseproject_rickmorty.utils.mapper.EpisodeToDbMapper
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class EpisodeRemoteMediator(
    private val mServices: RetrofitServices,
    private val db: ItemsDatabase,
    private val filterQueryList: MutableList<String>
) : RemoteMediator<Int, EpisodeForListDto>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EpisodeForListDto>
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
            val listEmpty = db.getEpisodeDao().getAll()
            if (listEmpty.isEmpty()) {
                isEmptyDb = true
            }
            val listQuery = db.getEpisodeDao().getSeveralForFilterCheck(filterQueryList[0], filterQueryList[1])
            if (listQuery.isEmpty()) {
                isEmptyQuery = true
            }
        }.start()

        try {
            delay(100)
            val response =
                mServices.getSeveralEpisodesFilter(page, filterQueryList[0], filterQueryList[1])
            val isEndOfList = response.info.next == null
            val queryList = filterQueryList[0] + filterQueryList[1]
            db.withTransaction {
                if (loadType == LoadType.REFRESH && queryList == "") {
                    db.getEpisodeCharacterJoinDao().deleteAll()
                    db.getEpisodeDao().deleteAll()
                    db.getEpisodeKeysDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.results.map {
                    EpisodeRemoteKey(
                        it.id.toString(),
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                db.getEpisodeKeysDao().insertAll(keys)
                db.getEpisodeDao().insertAll(EpisodeToDbMapper().transform(response.results))
                val listOfCharacterToEpisodes = EpisodeCharacterJoinMapper().transform(response.results)
                db.getEpisodeCharacterJoinDao().insertAll(listOfCharacterToEpisodes)
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
        state: PagingState<Int, EpisodeForListDto>
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

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, EpisodeForListDto>): EpisodeRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                db.getEpisodeKeysDao().remoteKeysEpisodeId(repoId.toString())
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, EpisodeForListDto>): EpisodeRemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { episode -> db.getEpisodeKeysDao().remoteKeysEpisodeId(episode.id.toString()) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, EpisodeForListDto>): EpisodeRemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { episode -> db.getEpisodeKeysDao().remoteKeysEpisodeId(episode.id.toString()) }
    }
}