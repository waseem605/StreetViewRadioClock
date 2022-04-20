package com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack

import com.liveearth.streetview.navigation.map.worldradio.streetViewPlacesNearMe.Result

interface StreetViewNearMeCallBack {
    fun onLocationInfo(model: Result)
    fun shareLocation(model: Result)
    fun addToFavouriteLocation(model: Result)
    fun removeFromFavouriteLocation(model: Result)
    fun onClickOfItemLocation(model: Result,pos:Int)
}