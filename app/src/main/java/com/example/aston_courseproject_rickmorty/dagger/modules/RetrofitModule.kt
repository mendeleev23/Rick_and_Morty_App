package com.example.aston_courseproject_rickmorty.dagger.modules

import com.example.aston_courseproject_rickmorty.model.retrofit.RetrofitServices
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {

    private val baseURL = "https://rickandmortyapi.com/api/"

    @Singleton
    @Provides
    fun getRetrofitServiceInterface(retrofit: Retrofit): RetrofitServices {
        return retrofit.create(RetrofitServices::class.java)
    }

    @Singleton
    @Provides
    fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}