package com.example.aston_courseproject_rickmorty.model.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.model.database.LocationDb
import com.example.aston_courseproject_rickmorty.model.database.LocationRemoteKey
import com.example.aston_courseproject_rickmorty.model.dto.LocationForListDto
import com.example.aston_courseproject_rickmorty.model.retrofit.RetrofitServices
import com.example.aston_courseproject_rickmorty.utils.mapper.LocationCharacterJoinMapper
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class LocationRemoteMediator(
    private val mServices: RetrofitServices,
    private val db: ItemsDatabase,
    private val filterQueryList: MutableList<String>
) : RemoteMediator<Int, LocationForListDto>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocationForListDto>
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
            val listEmpty = db.getLocationDao().getAll()
            if (listEmpty.isEmpty()) {
                isEmptyDb = true
            }
            val listQuery = db.getLocationDao().getSeveralForFilterCheck(filterQueryList[0], filterQueryList[1], filterQueryList[2])
            if (listQuery.isEmpty()) {
                isEmptyQuery = true
            }
        }.start()

        try {
            delay(100)
            val response = mServices.getSeveralLocationsFilter(page,filterQueryList[0], filterQueryList[1], filterQueryList[2])
            val isEndOfList = response.info.next == null
            val queryList = filterQueryList[0] + filterQueryList[1] + filterQueryList[2]
            db.withTransaction {
                if (loadType == LoadType.REFRESH && queryList == "") {
                    db.getLocationCharacterJoinDao().deleteAll()
                    db.getLocationDao().deleteAll()
                    db.getLocationKeysDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.results.map {
                    LocationRemoteKey(
                        it.id.toString(),
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                db.getLocationKeysDao().insertAll(keys)
                db.getLocationDao().insertAll(LocationDb.locationToDb(response.results))
                val listOfCharacterToEpisodes = LocationCharacterJoinMapper().transform(response.results)
                db.getLocationCharacterJoinDao().insertAll(listOfCharacterToEpisodes)
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
        state: PagingState<Int, LocationForListDto>
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

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, LocationForListDto>): LocationRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                db.getLocationKeysDao().remoteKeysLocationId(repoId.toString())
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, LocationForListDto>): LocationRemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { location -> db.getLocationKeysDao().remoteKeysLocationId(location.id.toString()) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, LocationForListDto>): LocationRemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { location -> db.getLocationKeysDao().remoteKeysLocationId(location.id.toString())}
    }
}