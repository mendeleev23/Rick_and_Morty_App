package com.example.aston_courseproject_rickmorty.model


data class Character(
    val id: Int? = null,
    val name: String? = null,
    val status: String? = null,
    val species: String? = null,
    val type: String? = null,
    val gender: String? = null,
    val origin: LocationForCharacter? = null,
    val location: LocationForCharacter?  = null,
    val image: String? = null,
    val episode: Array<String>?  = null,
    val url: String? = null,
    val created: String? = null
)

data class LocationForCharacter(val name: String?, val url: String?)

data class AllCharacters(val info: Info, val results: MutableList<Character>)