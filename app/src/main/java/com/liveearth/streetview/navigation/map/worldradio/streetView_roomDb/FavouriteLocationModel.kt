package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.mapbox.mapboxsdk.geometry.LatLng

@Entity(tableName = "FavouriteLocation_table")
class FavouriteLocationModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "locationId")
    var locationId: String?,
    @ColumnInfo(name = "address")
    var address: String?,
    @ColumnInfo(name = "name")
    var name: String?,
    @ColumnInfo(name = "timeZone")
    var timeZone: String?,
    @ColumnInfo(name = "date")
    var date: String?,
    @ColumnInfo(name = "time")
    var time: String?,
    @ColumnInfo(name = "latitude")
    var latitude: Double?,
    @ColumnInfo(name = "longitude")
    var longitude: Double?
)