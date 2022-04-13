package com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
    private val repositoryWeather: RepositoryWeather,
    private val lat: String,
    private val lon: String
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WeatherViewModel(repositoryWeather,lat,lon) as T
    }
}