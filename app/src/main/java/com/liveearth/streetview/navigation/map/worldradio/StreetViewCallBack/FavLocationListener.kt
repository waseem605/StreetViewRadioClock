package com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack

import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Favourite_roomDb.FavouriteLocationModel

interface FavLocationListener {
    fun onNavigateToLocation(model: FavouriteLocationModel)
    fun onShareFavLocation(model: FavouriteLocationModel)
    fun onDeleteFavLocation(model: FavouriteLocationModel)
}