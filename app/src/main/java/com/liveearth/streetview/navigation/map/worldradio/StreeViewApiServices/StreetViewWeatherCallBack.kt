package com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices

import com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather.StreetViewWeatherModel


interface StreetViewWeatherCallBack {
    fun onSuccess(data: StreetViewWeatherModel)
    fun onFailure(userError:String)
    fun onLoading(loading:Boolean)
}