package com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dummy.apiServices.WeatherAPI
import com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather.StreetViewWeatherModel

class RepositoryWeather(val service: WeatherAPI) {
    private val appId = "fef304ef226153ff5a6553f569892a4e"


    private val weatherLiveData = MutableLiveData<StreetViewWeatherModel>()

    val weatherList : LiveData<StreetViewWeatherModel>
    get() = weatherLiveData




    suspend fun getWeather(lat:String,long:String){
        val result = service.getWeatherDetailsMVVM(lat,long,appId)
        if (result?.body() !=null){
            weatherLiveData.postValue(result.body())

        }
    }




}