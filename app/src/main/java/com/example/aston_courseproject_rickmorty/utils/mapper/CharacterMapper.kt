package com.example.aston_courseproject_rickmorty.utils.mapper

import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.LocationForCharacter
import com.example.aston_courseproject_rickmorty.model.database.CharacterDb
import com.example.aston_courseproject_rickmorty.model.dto.CharacterDto
import com.example.aston_courseproject_rickmorty.model.dto.LocationForCharacterDto

class CharacterMapper(private val locationForCharacterMapper: Mapper<LocationForCharacter?, LocationForCharacterDto>) :
    Mapper<Character, CharacterDto> {
    override fun transform(data: Character): CharacterDto {
        var str = ""
        if (data.episode == null) {
            str = ""
        } else {
            for (url in data.episode) {
                val baseUrl = "https://rickandmortyapi.com/api/episode/"
                str += "${url.substring(baseUrl.length)},"
            }
            str = str.dropLast(1)
        }

        return CharacterDto(
            id = data.id,
            name = data.name,
            status = data.status,
            species = data.species,
            type = data.type,
            gender = data.gender,
            origin = locationForCharacterMapper.transform(data.origin),
            location = locationForCharacterMapper.transform(data.location),
            image = data.image,
            episode = str
        )
    }
}

class CharacterDbMapper(private val locationForCharacterMapper: Mapper<LocationForCharacter?, LocationForCharacterDto>, private val array: Array<Int>) :
    Mapper<CharacterDb, CharacterDto> {
    override fun transform(data: CharacterDb): CharacterDto {
        var str = ""
        for (url in array) {
            str += "${url},"
        }
        str = str.dropLast(1)
        return CharacterDto(
            id = data.id,
            name = data.name,
            status = data.status,
            species = data.species,
            type = data.type,
            gender = data.gender,
            origin = locationForCharacterMapper.transform(LocationForCharacter(data.origin_name, data.origin_url)),
            location = locationForCharacterMapper.transform(LocationForCharacter(data.location_name, data.location_url)),
            image = data.image,
            episode = str
        )
    }

}

class CharacterToDbMapper : Mapper<MutableList<Character>, MutableList<CharacterDb>> {
    override fun transform(data: MutableList<Character>): MutableList<CharacterDb> {
        val mutableList = mutableListOf<CharacterDb>()
        for (character in data) {
            mutableList.add(CharacterDb(
                id = character.id,
                name = character.name,
                status = character.status,
                species = character.species,
                type = character.type,
                gender = character.gender,
                origin_name = character.origin?.name,
                origin_url = character.origin?.url,
                location_name = character.location?.name,
                location_url = character.location?.url,
                image = character.image,
                url = character.url,
                created = character.created
            ))
        }
        return mutableList
    }
}

class LocationForCharacterMapper : Mapper<LocationForCharacter?, LocationForCharacterDto> {
    override fun transform(data: LocationForCharacter?): LocationForCharacterDto {
        val id: Int
        val baseUrl = "https://rickandmortyapi.com/api/location/"
        id = if (data?.url == "") {
            0
        } else {
            data?.url?.substring(baseUrl.length)?.toInt() ?: 0
        }
        return LocationForCharacterDto(data?.name, id)
    }
}