package com.liveearth.streetview.navigation.map.worldradio.roomdatabase

import androidx.room.TypeConverter
import java.util.*

class Convertors {

    @TypeConverter
    fun fromDateToLong(value: Date):Long{
        return value.time
    }

    @TypeConverter
    fun fromLongToDate(value: Long):Date{
        return Date(value)
    }

}