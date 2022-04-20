package com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack

import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.StreetViewModel

interface MainStreetViewCallBackListener {
    fun onClickShareCategory(model:StreetViewModel)
    fun onClickNavigateCategory(model:StreetViewModel)
    fun onClickNavigateLiveEarth(model:StreetViewModel)
}