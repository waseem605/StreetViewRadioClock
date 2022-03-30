package com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices

import com.liveearth.streetview.navigation.map.worldradio.streetViewPlacesNearMe.StreetViewNearPlacesModel


interface StreetViewNearByCallBack {
    fun onSuccess(data: StreetViewNearPlacesModel)
    fun onFailure(userError:String)
    fun onLoading(loading:Boolean)
}