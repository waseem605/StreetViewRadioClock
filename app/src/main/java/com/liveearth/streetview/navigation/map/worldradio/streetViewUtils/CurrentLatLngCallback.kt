package com.liveearth.streetview.navigation.map.worldradio.streetViewUtils

import com.mapbox.mapboxsdk.geometry.LatLng


interface CurrentLatLngCallback {
    fun onSuccessLatLng(latLngs: LatLng?)
    fun onFailedLatLng(error: String)
}