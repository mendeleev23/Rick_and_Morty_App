package com.example.aston_courseproject_rickmorty.dagger

import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.MainActivity
import com.example.aston_courseproject_rickmorty.dagger.modules.DataModule
import com.example.aston_courseproject_rickmorty.dagger.modules.RetrofitModule
import com.example.aston_courseproject_rickmorty.dagger.subcomponents.*
import dagger.Component
import javax.inject.Singleton

@ExperimentalPagingApi
@Singleton
@Component(modules = [AppModule::class, DataModule::class, RetrofitModule::class])
interface AppComponent {

    fun inject(mainActivity: MainActivity)

    fun getEpisodeComponentBuilder(): EpisodeComponent.Builder
    fun getEpisodeDetailsComponentBuilder(): EpisodeDetailsComponent.Builder
    fun getCharacterComponentBuilder(): CharacterComponent.Builder
    fun getCharacterDetailsComponentBuilder(): CharacterDetailsComponent.Builder
    fun getLocationComponentBuilder(): LocationComponent.Builder
    fun getLocationDetailsComponentBuilder(): LocationDetailsComponent.Builder

}
