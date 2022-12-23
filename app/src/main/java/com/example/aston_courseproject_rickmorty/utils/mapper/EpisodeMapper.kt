package com.example.aston_courseproject_rickmorty.utils.mapper

import com.example.aston_courseproject_rickmorty.model.Episode
import com.example.aston_courseproject_rickmorty.model.database.EpisodeDb
import com.example.aston_courseproject_rickmorty.model.dto.EpisodeDto

class EpisodeMapper : Mapper<Episode, EpisodeDto> {
    override fun transform(data: Episode): EpisodeDto {
        var str = ""
        if (data.characters == null) {
            str = ""
        } else {
            for (url in data.characters) {
                val baseUrl = "https://rickandmortyapi.com/api/character/"
                str += "${url.substring(baseUrl.length)},"
            }
            str = str.dropLast(1)
        }

        return EpisodeDto(
            id = data.id ?: 0,
            name = data.name ?: "",
            air_date = data.air_date ?: "",
            episode = data.episode ?: "",
            characters = str
        )
    }
}

class EpisodeDbMapper(private val array: Array<Int>?) : Mapper<EpisodeDb, EpisodeDto> {
    override fun transform(data: EpisodeDb): EpisodeDto {
        var str = ""
        if (array != null) {
            for (url in array) {
                str += "${url},"
            }
            str = str.dropLast(1)
        }

        return EpisodeDto(
            id = data.id ?: 0,
            name = data.name ?: "",
            air_date = data.air_date ?: "",
            episode = data.episode ?: "",
            characters = str
        )
    }
}


class EpisodeToDbMapper : Mapper<MutableList<Episode>, MutableList<EpisodeDb>> {
    override fun transform(data: MutableList<Episode>): MutableList<EpisodeDb> {
        val newMutableList = mutableListOf<EpisodeDb>()
        for (episode in data) {
            newMutableList.add(EpisodeDb(
                id = episode.id,
                name = episode.name,
                air_date = episode.air_date,
                episode = episode.episode,
                url = episode.url,
                created = episode.created
            ))
        }
        return newMutableList
    }
}