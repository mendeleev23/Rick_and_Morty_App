package com.example.aston_courseproject_rickmorty.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.example.aston_courseproject_rickmorty.model.dto.EpisodeForListDto
import com.example.aston_courseproject_rickmorty.viewmodel.EpisodeViewModel
import kotlinx.coroutines.flow.Flow


@ExperimentalPagingApi
class EpisodeViewModelFactory(private val dataSource: Flow<PagingData<EpisodeForListDto>>) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EpisodeViewModel(dataSource) as T
    }
}