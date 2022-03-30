package com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack

import com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather.StreetViewWeatherModel

interface WeatherCallBack {
    fun onItemWeather(model:StreetViewWeatherModel)
}