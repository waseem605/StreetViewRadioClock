package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.mapbox.mapboxsdk.geometry.LatLng

@Entity(tableName = "timeZoneWorld_table")
class FavouriteLocationModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "name")
    var name: String?,
    @ColumnInfo(name = "iso")
    var iso: String?,
    @ColumnInfo(name = "timeZone")
    var timeZone: String?
)