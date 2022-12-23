package com.example.aston_courseproject_rickmorty.utils.mapper

import com.example.aston_courseproject_rickmorty.model.Location
import com.example.aston_courseproject_rickmorty.model.dto.LocationDto
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class LocationMapperTest {

    lateinit var SUT: LocationMapper

    @Before
    fun setUp() {
        SUT = LocationMapper()
    }

    @Test
    fun transform_usualLocation_usualLocationDto() {
        val input = Location(7, "Immortality Field Resort", "Resort", "unknown", arrayOf("https://rickandmortyapi.com/api/character/23","https://rickandmortyapi.com/api/character/204","https://rickandmortyapi.com/api/character/320"), "https://rickandmortyapi.com/api/location/7", "2017-11-10T13:09:17.136Z")
        val expected = LocationDto(7, "Immortality Field Resort", "Resort", "unknown", "23,204,320")
        val result = SUT.transform(input)
        assertEquals(result, expected)
    }

    @Test
    fun transform_emptyArrayOfCharactersLocation_usualLocationDto() {
        val input = Location(2, "Abadango", "Cluster", "unknown", arrayOf(), "https://rickandmortyapi.com/api/location/2", "2017-11-10T13:06:38.182Z")
        val expected = LocationDto(2, "Abadango", "Cluster", "unknown", "")
        val result = SUT.transform(input)
        assertEquals(result, expected)
    }

    @Test
    fun transform_nullArrayOfCharactersLocation_usualLocationDto() {
        val input = Location(2, "Abadango", "Cluster", "unknown", null, "https://rickandmortyapi.com/api/location/2", "2017-11-10T13:06:38.182Z")
        val expected = LocationDto(2, "Abadango", "Cluster", "unknown", "")
        val result = SUT.transform(input)
        assertEquals(result, expected)
    }

    @Test
    fun transform_locationOfNulls_emptyLocationDto() {
        val input = Location(null, null, null, null, null, null, null)
        val expected = LocationDto(0, "", "", "", "")
        val result = SUT.transform(input)
        assertEquals(result, expected)
    }

    @Test
    fun transform_ArrayWithOneCharacterLocation_usualLocationDto() {
        val input = Location(2, "Abadango", "Cluster", "unknown", arrayOf("https://rickandmortyapi.com/api/character/6"), "https://rickandmortyapi.com/api/location/2", "2017-11-10T13:06:38.182Z")
        val expected = LocationDto(2, "Abadango", "Cluster", "unknown", "6")
        val result = SUT.transform(input)
        assertEquals(result, expected)
    }
}