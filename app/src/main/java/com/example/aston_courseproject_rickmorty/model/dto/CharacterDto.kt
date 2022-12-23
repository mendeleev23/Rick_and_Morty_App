package com.example.aston_courseproject_rickmorty.model.dto


data class CharacterDto(
    val id: Int? = null,
    val name: String? = null,
    val status: String? = null,
    val species: String? = null,
    val type: String? = null,
    val gender: String? = null,
    val origin: LocationForCharacterDto? = null,
    val location: LocationForCharacterDto?  = null,
    val image: String? = null,
    val episode: String?  = null
)

data class LocationForCharacterDto(val name: String?, val locationId: Int?)

data class CharacterForListDto(
    val id: Int?,
    val name: String?,
    val status: String?,
    val species: String?,
    val gender: String?,
    val image: String?
)