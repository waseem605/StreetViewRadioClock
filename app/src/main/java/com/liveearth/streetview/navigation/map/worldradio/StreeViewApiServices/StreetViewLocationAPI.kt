package com.example.weatherapp.services

import com.liveearth.streetview.navigation.map.worldradio.streetViewPlacesNearMe.StreetViewNearPlacesModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface StreetViewLocationAPI {

    @GET("/v3/places/nearby")
    fun getNearByPlace(
        @Query("client_id") client_id: String,
        @Query("client_secret") client_secret: String,
        @Query("v") v: String,
        @Query("radius") radius: String,
        @Query("ll") ll: String,
        @Query("limit") limit: String,
        @Query("categories") categories: String,
        @Header("Authorization") auth: String
    ): Call<StreetViewNearPlacesModel>

    @GET("/v3/places/search")
    fun getPlacesSearchById(
        @Query("client_id") client_id: String,
        @Query("client_secret") client_secret: String,
        @Query("v") v: String,
        @Query("radius") radius: String,
        @Query("ll") ll: String,
        @Query("limit") limit: String,
        @Query("categories") categories: String,
        @Header("Authorization") auth: String
    ): Call<StreetViewNearPlacesModel>


    /*   @GET("v2/venues/search?client_id=3VVMLJA0O4TSYWRU2JLSTHXC03LBKM0AUMZQRLWWW11ANSCL&client_secret=EXCT51N1YTRDUILYUIUAXH00YKAIZHXZIEFNZ1FE3HD1XHC4&v=20201114&radius=20000&limit=50&ll=33.52223922,73.1538862")
       fun getData(): Single<FourSquareMainModel>*/

}