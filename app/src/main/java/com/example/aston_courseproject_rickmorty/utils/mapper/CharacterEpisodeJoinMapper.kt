package com.example.aston_courseproject_rickmorty.utils.mapper

import com.example.aston_courseproject_rickmorty.model.Character
import com.example.aston_courseproject_rickmorty.model.database.CharacterEpisodeJoin

class CharacterEpisodeJoinMapper : Mapper<MutableList<Character>, MutableList<CharacterEpisodeJoin>> {
    override fun transform(data: MutableList<Character>): MutableList<CharacterEpisodeJoin> {
        val listToDb: MutableList<CharacterEpisodeJoin> = mutableListOf()
        for (character in data) {
            for (i in 0 until character.episode?.size!!) {
                val baseUrl = "https://rickandmortyapi.com/api/episode/"
                val episodeId = character.episode[i].substring(baseUrl.length).toInt()
                val ceJoin = CharacterEpisodeJoin(character.id!!, episodeId)
                listToDb.add(ceJoin)
            }
        }
        return listToDb
    }
}