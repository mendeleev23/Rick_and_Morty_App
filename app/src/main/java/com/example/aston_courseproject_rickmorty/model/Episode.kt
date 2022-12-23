package com.example.aston_courseproject_rickmorty.model


data class Episode(
    val id: Int? = null,
    val name: String? = null,
    val air_date: String? = null,
    val episode: String? = null,
    val characters: Array<String>? = null,
    val url: String? = null,
    val created: String? = null
)

data class AllEpisodes(val info: Info, val results: MutableList<Episode>)