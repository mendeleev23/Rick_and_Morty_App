package com.example.aston_courseproject_rickmorty.model.retrofit

import com.example.aston_courseproject_rickmorty.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RetrofitServices {

    @GET("character/{id}")
    suspend fun getOneCharacter(@Path("id") id: Int): Character

    @GET("character/{ids}")
    suspend fun getSeveralCharacters(@Path("ids") ids: String): MutableList<Character>

    @GET("character")
    suspend fun getSeveralCharactersFilter(
        @Query("page") queryPage: Int,
        @Query("name") queryName: String,
        @Query("status") queryStatus: String,
        @Query("species") querySpecies: String,
        @Query("type") queryType: String,
        @Query("gender") queryGender: String
    ): AllCharacters


    @GET("location/{id}")
    suspend fun getOneLocation(@Path("id") id: Int): Location

    @GET("location")
    suspend fun getSeveralLocationsFilter(
        @Query("page") queryPage: Int,
        @Query("name") queryName: String,
        @Query("type") queryType: String,
        @Query("dimension") queryDimension: String
    ): AllLocations


    @GET("episode/{id}")
    suspend fun getOneEpisode(@Path("id") id: Int): Episode

    @GET("episode/{ids}")
    suspend fun getSeveralEpisodes(@Path("ids") ids: String): MutableList<Episode>

    @GET("episode")
    suspend fun getSeveralEpisodesFilter(
        @Query("page") queryPage: Int,
        @Query("name") queryName: String,
        @Query("episode") queryEpisode: String
    ): AllEpisodes
}