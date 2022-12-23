package com.example.aston_courseproject_rickmorty.viewmodel.factory


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.model.repository.CharacterDetailsRepository
import com.example.aston_courseproject_rickmorty.utils.InternetConnectionChecker
import com.example.aston_courseproject_rickmorty.viewmodel.CharacterDetailsViewModel

@ExperimentalPagingApi
class CharacterDetailsViewModelFactory(
    private val characterID: Int,
    val repository: CharacterDetailsRepository,
    private val internetChecker: InternetConnectionChecker
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharacterDetailsViewModel(characterID, repository, internetChecker) as T
    }
}