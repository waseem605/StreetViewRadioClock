package com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack

import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.WorldClockModel

interface WorldClockCallBack {
    fun onItemWorldClock()
    fun onClickAddTimeZone(model: WorldClockModel)
}