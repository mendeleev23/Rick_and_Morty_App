package com.example.aston_courseproject_rickmorty.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.example.aston_courseproject_rickmorty.model.dto.LocationForListDto
import com.example.aston_courseproject_rickmorty.viewmodel.LocationViewModel
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class LocationViewModelFactory(private val dataSource: Flow<PagingData<LocationForListDto>>) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationViewModel(dataSource) as T
    }
}