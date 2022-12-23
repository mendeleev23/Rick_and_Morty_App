package com.example.aston_courseproject_rickmorty.utils.mapper

import com.example.aston_courseproject_rickmorty.model.Location
import com.example.aston_courseproject_rickmorty.model.database.LocationDb
import com.example.aston_courseproject_rickmorty.model.dto.LocationDto

class LocationMapper : Mapper<Location, LocationDto> {
    override fun transform(data: Location): LocationDto {
        var str = ""
        if (data.residents == null) {
            str = ""
        } else {
            for (url in data.residents) {
                val baseUrl = "https://rickandmortyapi.com/api/character/"
                str += "${url.substring(baseUrl.length)},"
            }
            str = str.dropLast(1)
        }

        return LocationDto(
            id = data.id ?: 0,
            name = data.name ?: "",
            type = data.type ?: "",
            dimension = data.dimension ?: "",
            residents = str
        )
    }
}

class LocationDbMapper(private val array: Array<Int>) : Mapper<LocationDb, LocationDto> {
    override fun transform(data: LocationDb): LocationDto {
        var str = ""
        for (url in array) {
            str += "${url},"
        }
        str = str.dropLast(1)

        return LocationDto(
            id = data.id ?: 0,
            name = data.name ?: "",
            type = data.type ?: "",
            dimension = data.dimension ?: "",
            residents = str
        )
    }

}