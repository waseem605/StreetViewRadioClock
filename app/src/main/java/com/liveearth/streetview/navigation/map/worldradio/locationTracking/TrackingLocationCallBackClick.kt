package com.liveearth.streetview.navigation.map.worldradio.locationTracking

import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.TrackingLocationModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Tracking_roomDb.TrackLocationModel

interface TrackingLocationCallBackClick {
    fun onClickItemLocation(model:TrackLocationModel,pos :Int)
}