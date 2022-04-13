package com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack

import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.CategoryStreetViewModel

interface CategoryStreetViewCallBackListener {
    fun onClickCategory(model:CategoryStreetViewModel,pos:Int)
}