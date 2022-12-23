package com.example.aston_courseproject_rickmorty.dagger.modules


import android.content.Context
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.view.dialogs.CharacterFilterDialog
import com.example.aston_courseproject_rickmorty.view.recycler_view.CharacterPaginationRecyclerAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Named

@ExperimentalPagingApi
@Module
class CharacterModule {

    @Provides
    fun provideCharacterPaginationRecyclerAdapter(itemClickListener: CharacterPaginationRecyclerAdapter.CharacterViewHolder.ItemClickListener): CharacterPaginationRecyclerAdapter {
        return CharacterPaginationRecyclerAdapter(itemClickListener)
    }

    @Provides
    fun provideCharacterDialogProcessor(@Named("characterContext") fragmentContext: Context, applyClickListener: CharacterFilterDialog.ApplyClickListener): CharacterFilterDialog {
        return CharacterFilterDialog(fragmentContext, applyClickListener)
    }
}