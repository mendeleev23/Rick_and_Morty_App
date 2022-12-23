package com.example.aston_courseproject_rickmorty.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.aston_courseproject_rickmorty.model.dto.LocationForListDto

class LocationDiffUtilCallback(
    private val oldList: List<LocationForListDto>,
    private val newList: MutableList<LocationForListDto>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldLocation: LocationForListDto = oldList[oldItemPosition]
        val newLocation: LocationForListDto = newList[newItemPosition]
        return oldLocation.id == newLocation.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldLocation: LocationForListDto = oldList[oldItemPosition]
        val newLocation: LocationForListDto = newList[newItemPosition]

        return (oldLocation.name == newLocation.name
                && oldLocation.type == newLocation.type
                && oldLocation.dimension == newLocation.dimension)
    }
}