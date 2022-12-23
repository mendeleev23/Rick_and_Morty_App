package com.example.aston_courseproject_rickmorty.model.dto


data class LocationDto(
    val id: Int = 0,
    val name: String = "",
    val type: String = "",
    val dimension: String = "",
    val residents: String = ""
)

data class LocationForListDto(
    val id: Int? = null,
    val name: String? = null,
    val type: String? = null,
    val dimension: String? = null,
)