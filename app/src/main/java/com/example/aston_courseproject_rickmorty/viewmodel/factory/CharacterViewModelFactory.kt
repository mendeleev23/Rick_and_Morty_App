package com.example.aston_courseproject_rickmorty.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.example.aston_courseproject_rickmorty.model.dto.CharacterForListDto
import com.example.aston_courseproject_rickmorty.viewmodel.CharacterViewModel
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class CharacterViewModelFactory(private val dataSource: Flow<PagingData<CharacterForListDto>>) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharacterViewModel(dataSource) as T
    }
}