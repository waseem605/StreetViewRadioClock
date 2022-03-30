package com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather

data class StreetViewWeatherModel(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherList>,
    val message: Int
)