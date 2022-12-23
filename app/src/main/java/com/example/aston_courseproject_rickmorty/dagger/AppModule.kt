package com.example.aston_courseproject_rickmorty.dagger

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.MainViewModelFactory
import com.example.aston_courseproject_rickmorty.utils.ResourceProvider
import dagger.Module
import dagger.Provides

@ExperimentalPagingApi
@Module
class AppModule(val context: Application) {

    @Provides
    fun provideContext(): Application {
        return context
    }

    @Provides
    fun provideMainViewModelFactory(mResourceProvider: ResourceProvider) : MainViewModelFactory {
        return MainViewModelFactory(mResourceProvider)
    }

}
