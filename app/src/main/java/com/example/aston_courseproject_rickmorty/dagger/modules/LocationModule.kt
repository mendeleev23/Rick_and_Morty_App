package com.example.aston_courseproject_rickmorty.dagger.modules


import android.content.Context
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.view.dialogs.LocationFilterDialog
import com.example.aston_courseproject_rickmorty.view.recycler_view.LocationPaginationRecyclerAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Named

@ExperimentalPagingApi
@Module
class LocationModule {

    @Provides
    fun provideLocationPaginationRecyclerAdapter(itemClickListener: LocationPaginationRecyclerAdapter.LocationViewHolder.ItemClickListener): LocationPaginationRecyclerAdapter {
        return LocationPaginationRecyclerAdapter(itemClickListener)
    }

    @Provides
    fun provideLocationDialogProcessor(@Named("locationContext") fragmentContext: Context, applyClickListener: LocationFilterDialog.ApplyClickListener): LocationFilterDialog {
        return LocationFilterDialog(fragmentContext, applyClickListener)
    }
}