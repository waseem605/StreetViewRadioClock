package com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack

import android.location.Location

interface MyLocationListener {
    fun onLocationChanged(location: Location)
}