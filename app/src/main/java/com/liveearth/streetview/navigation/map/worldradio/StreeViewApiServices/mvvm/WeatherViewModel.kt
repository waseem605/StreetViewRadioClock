package com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather.StreetViewWeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repositoryWeather: RepositoryWeather,
    private val lat: String,
    private val lon: String
):ViewModel() {


    init {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryWeather.getWeather(lat,lon )
        }

    }

    val weather:LiveData<StreetViewWeatherModel>
    get() = repositoryWeather.weatherList


}