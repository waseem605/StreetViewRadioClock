package com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.mvvm

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    private val BASE_URL = "https://api.openweathermap.org/"
    private val appId = "fef304ef226153ff5a6553f569892a4e"


    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

}