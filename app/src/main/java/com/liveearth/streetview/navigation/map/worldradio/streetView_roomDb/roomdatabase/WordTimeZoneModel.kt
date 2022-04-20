package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.roomdatabase

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "timeZoneWorld_table")
class WordTimeZoneModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "country")
    var country: String?,
    @ColumnInfo(name = "iso")
    var iso: String?,
    @ColumnInfo(name = "timeZone")
    var timeZone: String?
)