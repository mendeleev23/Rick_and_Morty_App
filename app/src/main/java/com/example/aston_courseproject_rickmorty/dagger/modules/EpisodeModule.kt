package com.example.aston_courseproject_rickmorty.dagger.modules

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.view.dialogs.EpisodeFilterDialog
import com.example.aston_courseproject_rickmorty.view.recycler_view.EpisodePaginationRecyclerAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Named

@ExperimentalPagingApi
@Module
class EpisodeModule {

    @Provides
    fun provideEpisodePaginationRecyclerAdapter(itemClickListener: EpisodePaginationRecyclerAdapter.EpisodeViewHolder.ItemClickListener): EpisodePaginationRecyclerAdapter {
        return EpisodePaginationRecyclerAdapter(itemClickListener)
    }

    @Provides
    fun provideEpisodeDialogProcessor(@Named("episodeContext") fragmentContext: Context, applyClickListener: EpisodeFilterDialog.ApplyClickListener): EpisodeFilterDialog {
        return EpisodeFilterDialog(fragmentContext, applyClickListener)
    }
}