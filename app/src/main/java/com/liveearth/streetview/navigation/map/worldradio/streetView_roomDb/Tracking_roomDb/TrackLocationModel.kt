package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Tracking_roomDb

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.mapbox.mapboxsdk.geometry.LatLng

@Entity(tableName = "TrackingLocation_table")
class TrackLocationModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "date")
    var date: String?,
    @ColumnInfo(name = "time")
    var time: String?,
    @ColumnInfo(name = "latitude")
    var latitude: Double?,
    @ColumnInfo(name = "longitude")
    var longitude: Double?
)