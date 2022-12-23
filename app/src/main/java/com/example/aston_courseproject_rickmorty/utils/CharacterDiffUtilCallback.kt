package com.example.aston_courseproject_rickmorty.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.aston_courseproject_rickmorty.model.dto.CharacterForListDto

class CharacterDiffUtilCallback(
    private val oldList: List<CharacterForListDto>,
    private val newList: MutableList<CharacterForListDto>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCharacter: CharacterForListDto = oldList[oldItemPosition]
        val newCharacter: CharacterForListDto = newList[newItemPosition]
        return oldCharacter.id == newCharacter.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCharacter: CharacterForListDto = oldList[oldItemPosition]
        val newCharacter: CharacterForListDto = newList[newItemPosition]

        return (oldCharacter.name == newCharacter.name
                && oldCharacter.species == newCharacter.species
                && oldCharacter.status == newCharacter.status
                && oldCharacter.gender == newCharacter.gender)
    }
}