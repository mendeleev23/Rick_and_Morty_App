package com.example.aston_courseproject_rickmorty.dagger.modules

import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.model.repository.CharacterDetailsRepository
import com.example.aston_courseproject_rickmorty.utils.InternetConnectionChecker
import com.example.aston_courseproject_rickmorty.viewmodel.factory.CharacterDetailsViewModelFactory
import dagger.Module
import dagger.Provides

@ExperimentalPagingApi
@Module
class CharacterDetailsModule {

    @Provides
    fun provideCharacterDetailsViewModelFactory(characterId: Int, repository: CharacterDetailsRepository, internetChecker: InternetConnectionChecker): CharacterDetailsViewModelFactory {
        return CharacterDetailsViewModelFactory(characterId, repository, internetChecker)
    }
/*
    @Provides
    fun provideCharacterPaginationRecyclerAdapter(itemClickListener: CharacterPaginationRecyclerAdapter.CharacterViewHolder.ItemClickListener): CharacterPaginationRecyclerAdapter {
        return CharacterPaginationRecyclerAdapter(itemClickListener)
    }

    @Provides
    fun provideCharacterPaginationRecyclerAdapter(itemClickListener: CharacterPaginationRecyclerAdapter.CharacterViewHolder.ItemClickListener): CharacterPaginationRecyclerAdapter {
        return CharacterPaginationRecyclerAdapter(itemClickListener)
    }*/
}