package com.example.aston_courseproject_rickmorty.dagger.subcomponents

import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.dagger.modules.CharacterDetailsModule
import com.example.aston_courseproject_rickmorty.view.fragments.CharacterDetailsFragment
import com.example.aston_courseproject_rickmorty.view.recycler_view.EpisodeRecyclerAdapter
import com.example.aston_courseproject_rickmorty.view.recycler_view.LocationRecyclerAdapter
import dagger.BindsInstance
import dagger.Subcomponent

@ExperimentalPagingApi
@Subcomponent(modules = [CharacterDetailsModule::class])
interface CharacterDetailsComponent {

    fun inject(characterDetailsFragment: CharacterDetailsFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun characterId(characterId: Int): Builder
        @BindsInstance
        fun episodeItemClickListener(itemClickListener: EpisodeRecyclerAdapter.EpisodeViewHolder.ItemClickListener): Builder
        @BindsInstance
        fun locationItemClickListener(itemClickListener: LocationRecyclerAdapter.LocationViewHolder.ItemClickListener): Builder
        fun build(): CharacterDetailsComponent
    }
}