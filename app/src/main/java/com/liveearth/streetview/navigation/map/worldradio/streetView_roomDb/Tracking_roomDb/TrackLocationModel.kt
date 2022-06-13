package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Tracking_roomDb

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.ExpenseItemModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.TrackingLocationModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseItemTypeConverters
import com.mapbox.mapboxsdk.geometry.LatLng

@Entity(tableName = "TrackingLocation_table")
class TrackLocationModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "date")
    var date: String?,
    @ColumnInfo(name = "startTime")
    var startTime: String?,
    @ColumnInfo(name = "time")
    var time: String?,
    @ColumnInfo(name = "avgSpeed")
    var avgSpeed: Float?,
    @ColumnInfo(name = "distance")
    var distance: Double?,
    @TypeConverters(TrackLocationItemTypeConverters::class)
    @ColumnInfo(name = "locationsList")
    var locationsList: List<TrackingLocationModel>,
)