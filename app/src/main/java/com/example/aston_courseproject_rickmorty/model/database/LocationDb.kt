package com.example.aston_courseproject_rickmorty.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.aston_courseproject_rickmorty.model.Location

@Entity(tableName = "locations_full_info")
class LocationDb(
    @PrimaryKey val id: Int? = null,
    val name: String? = null,
    val type: String? = null,
    val dimension: String? = null,
    val url: String? = null,
    val created: String? = null
) {
    companion object {
        fun locationToDb(location: Location): LocationDb {
            return LocationDb(
                id = location.id,
                name = location.name,
                type = location.type,
                dimension = location.dimension,
                url = location.url,
                created = location.created
            )
        }

        fun locationToDb(locations: MutableList<Location>): MutableList<LocationDb> {
            val newMutableList = mutableListOf<LocationDb>()
            for (location in locations) {
                newMutableList.add(locationToDb(location))
            }
            return newMutableList
        }
    }
}

class LocationForListDb(
    val id: Int? = null,
    val name: String? = null,
    val type: String? = null,
    val dimension: String? = null
)