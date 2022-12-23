package com.example.aston_courseproject_rickmorty.dagger.modules

import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.model.repository.EpisodeDetailsRepository
import com.example.aston_courseproject_rickmorty.utils.InternetConnectionChecker
import com.example.aston_courseproject_rickmorty.viewmodel.factory.EpisodeDetailsViewModelFactory
import dagger.Module
import dagger.Provides

@ExperimentalPagingApi
@Module
class EpisodeDetailsModule {

    @Provides
    fun provideEpisodeDetailsViewModelFactory(episodeId: Int, repository: EpisodeDetailsRepository, internetChecker: InternetConnectionChecker): EpisodeDetailsViewModelFactory {
        return EpisodeDetailsViewModelFactory(episodeId, repository, internetChecker)
    }
}