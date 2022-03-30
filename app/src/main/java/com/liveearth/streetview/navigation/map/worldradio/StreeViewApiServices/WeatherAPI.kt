package com.example.dummy.apiServices


import com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather.StreetViewWeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface WeatherAPI {

  @GET("data/2.5/forecast")
  fun getWeatherDetails(
                     @Query("lat")ll: String,
                     @Query("lon")long: String,
                     @Query("appid")appid: String): Call<StreetViewWeatherModel>



 /*   @GET("v2/venues/search?client_id=3VVMLJA0O4TSYWRU2JLSTHXC03LBKM0AUMZQRLWWW11ANSCL&client_secret=EXCT51N1YTRDUILYUIUAXH00YKAIZHXZIEFNZ1FE3HD1XHC4&v=20201114&radius=20000&limit=50&ll=33.52223922,73.1538862")
    fun getData(): Single<FourSquareMainModel>*/

}