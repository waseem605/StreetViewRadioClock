package com.liveearth.streetview.navigation.map.worldradio.locationTracking

import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.TrackingLocationModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Tracking_roomDb.TrackLocationModel

interface TrackingLocationCallBackListener {
    fun onChangeLocation(model:TrackingLocationModel)

}