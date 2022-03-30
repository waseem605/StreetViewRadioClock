package com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack

import com.liveearth.streetview.navigation.map.worldradio.streetViewPlacesNearMe.Result

interface StreetViewNearMeCallBack {
    fun onLocationInfo(model: Result)
}