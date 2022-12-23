package com.example.aston_courseproject_rickmorty.model.dto


data class EpisodeDto(
    val id: Int = 0,
    val name: String = "",
    val air_date: String = "",
    val episode: String = "",
    val characters: String = ""
)

data class EpisodeForListDto(
    val id: Int? = null,
    val name: String? = null,
    val air_date: String? = null,
    val episode: String? = null,
)