package com.example.aston_courseproject_rickmorty.model


data class Location(
    val id: Int? = null,
    val name: String? = null,
    val type: String? = null,
    val dimension: String? = null,
    val residents: Array<String>? = null,
    val url: String? = null,
    val created: String? = null
)

data class AllLocations(val info: Info, val results: MutableList<Location>)