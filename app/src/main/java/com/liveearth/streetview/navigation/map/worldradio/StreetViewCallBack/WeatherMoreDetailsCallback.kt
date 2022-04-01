package com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack

import com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather.WeatherList


interface WeatherMoreDetailsCallback {
    fun onItemClickWeather(weatherList: WeatherList)
}