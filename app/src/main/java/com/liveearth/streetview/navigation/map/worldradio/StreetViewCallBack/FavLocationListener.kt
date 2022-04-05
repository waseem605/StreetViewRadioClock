package com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack

import android.location.Location
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.FavouriteLocationModel

interface FavLocationListener {
    fun onNavigateToLocation(model:FavouriteLocationModel)
    fun onShareFavLocation(model:FavouriteLocationModel)
    fun onDeleteFavLocation(model:FavouriteLocationModel)
}