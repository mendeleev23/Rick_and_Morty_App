package com.example.aston_courseproject_rickmorty.viewmodel.factory


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.model.repository.EpisodeDetailsRepository
import com.example.aston_courseproject_rickmorty.utils.InternetConnectionChecker
import com.example.aston_courseproject_rickmorty.viewmodel.EpisodeDetailsViewModel

@ExperimentalPagingApi
class EpisodeDetailsViewModelFactory(
    private val episodeID: Int,
    val repository: EpisodeDetailsRepository,
    private val internetChecker: InternetConnectionChecker
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EpisodeDetailsViewModel(episodeID, repository, internetChecker) as T
    }
}