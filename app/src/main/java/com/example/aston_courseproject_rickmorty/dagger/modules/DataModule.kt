package com.example.aston_courseproject_rickmorty.dagger.modules

import android.app.Application
import com.example.aston_courseproject_rickmorty.model.database.ItemsDatabase
import com.example.aston_courseproject_rickmorty.model.repository.*
import com.example.aston_courseproject_rickmorty.model.retrofit.RetrofitServices
import com.example.aston_courseproject_rickmorty.utils.InternetConnectionChecker
import com.example.aston_courseproject_rickmorty.utils.ResourceProvider
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    fun provideResourceProvider(context: Application): ResourceProvider {
        return ResourceProvider(context)
    }

    @Provides
    fun provideInternetConnectionChecker(context: Application): InternetConnectionChecker {
        return InternetConnectionChecker(context)
    }

    @Provides
    @Singleton
    fun provideItemsDatabase(context: Application): ItemsDatabase {
        return ItemsDatabase.getDatabase(context)
    }

    @Provides
    fun provideCharacterRepository(mService: RetrofitServices, database: ItemsDatabase): CharacterRepository {
        return CharacterRepository(mService, database)
    }

    @Provides
    fun provideEpisodeRepository(mService: RetrofitServices, database: ItemsDatabase): EpisodeRepository {
        return EpisodeRepository(mService, database)
    }

    @Provides
    fun provideLocationRepository(mService: RetrofitServices, database: ItemsDatabase): LocationRepository {
        return LocationRepository(mService, database)
    }

    @Provides
    fun provideCharacterDetailsRepository(mService: RetrofitServices, database: ItemsDatabase): CharacterDetailsRepository {
        return CharacterDetailsRepository(mService, database)
    }

    @Provides
    fun provideEpisodeDetailsRepository(mService: RetrofitServices, database: ItemsDatabase): EpisodeDetailsRepository {
        return EpisodeDetailsRepository(mService, database)
    }

    @Provides
    fun provideLocationDetailsRepository(mService: RetrofitServices, database: ItemsDatabase, dispatcher: CoroutineDispatcher): LocationDetailsRepository {
        return LocationDetailsRepository(mService, database, dispatcher)
    }

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}