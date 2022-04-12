package com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack

import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.WorldClockModel
import java.util.*

interface WorldClockCallBack {
    fun onItemWorldClock(time:String)
    fun onClickAddTimeZone(model: WorldClockModel)
}