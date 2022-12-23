package com.example.aston_courseproject_rickmorty.dagger.subcomponents

import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.dagger.modules.LocationDetailsModule
import com.example.aston_courseproject_rickmorty.view.fragments.LocationDetailsFragment
import com.example.aston_courseproject_rickmorty.view.recycler_view.CharacterRecyclerAdapter
import dagger.BindsInstance
import dagger.Subcomponent

@ExperimentalPagingApi
@Subcomponent(modules = [LocationDetailsModule::class])
interface LocationDetailsComponent {
    fun inject(locationDetailsFragment: LocationDetailsFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun locationId(locationId: Int): Builder
        @BindsInstance
        fun characterItemClickListener(itemClickListener: CharacterRecyclerAdapter.CharacterViewHolder.ItemClickListener): Builder
        fun build(): LocationDetailsComponent
    }
}