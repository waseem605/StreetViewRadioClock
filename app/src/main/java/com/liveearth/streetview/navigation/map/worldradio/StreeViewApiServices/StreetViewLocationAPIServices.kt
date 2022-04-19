package com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices

import android.util.Log
import com.example.weatherapp.services.StreetViewLocationAPI
import com.liveearth.streetview.navigation.map.worldradio.streetViewPlacesNearMe.StreetViewNearPlacesModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class StreetViewLocationAPIServices(val streetViewNearByCallBack: StreetViewNearByCallBack) {
    val TAG = "LocationAPIServices"
    private val BASE_URL = "https://api.foursquare.com/"
    private val client_id = "JQBDDFQMSZLM01T4DMB2BXUEPIWQDPAT3RIK1DFF3ZZ3XDAB"
    private val client_secret = "WVTNEI053XK1TZ5SYNOZ43BII4KZJRFUQKI5ID0KGKKULNPL"
    private val v = "20211231"
    private val redius = "10000"
    private val limit = "50"
    private val Authorization = "fsq3XOG8KKHZjBz8MAxSSYCyBsTWowCODOBWTGunZ4a28Cc="


    fun getDataServiceNearPlaces(ll:String,category:String) {

        val client = OkHttpClient.Builder()
        client.connectTimeout(30, TimeUnit.SECONDS)
        client.readTimeout(30, TimeUnit.SECONDS)
        client.readTimeout(30, TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        client.addInterceptor(interceptor)

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
            .create(StreetViewLocationAPI::class.java)

        val call= api.getNearByPlace(client_id,client_secret,v,redius,ll,limit,category,Authorization)

        call.enqueue(object :Callback<StreetViewNearPlacesModel>{
            override fun onResponse(
                call: Call<StreetViewNearPlacesModel>,
                response: Response<StreetViewNearPlacesModel>
            ) {
                if (response.isSuccessful) {
                    streetViewNearByCallBack.onLoading(false)
                    streetViewNearByCallBack.onSuccess(response.body()!!)
                }else{
                    streetViewNearByCallBack.onLoading(false)
                    streetViewNearByCallBack.onFailure(response.errorBody()!!.toString())
                }
            }

            override fun onFailure(call: Call<StreetViewNearPlacesModel>, t: Throwable) {
                streetViewNearByCallBack.onLoading(false)
                streetViewNearByCallBack.onFailure(t.message!!)
            }

        })
    }

    fun getDataServiceBySearchId(ll:String,category:String) {

       // val category = ""

        val client = OkHttpClient.Builder()
        client.connectTimeout(30, TimeUnit.SECONDS)
        client.readTimeout(30, TimeUnit.SECONDS)
        client.readTimeout(30, TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        client.addInterceptor(interceptor)

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
            .create(StreetViewLocationAPI::class.java)

        val call= api.getNearByPlace(client_id,client_secret,v,redius,ll,limit,category,Authorization)
//        val call= api.getNearByPlace(client_id,client_secret,v,redius,ll,limit,category,Authorization)

        call.enqueue(object :Callback<StreetViewNearPlacesModel>{
            override fun onResponse(
                call: Call<StreetViewNearPlacesModel>,
                response: Response<StreetViewNearPlacesModel>
            ) {
                if (response.isSuccessful) {
                    streetViewNearByCallBack.onLoading(false)
                    streetViewNearByCallBack.onSuccess(response.body()!!)
                    Log.d(TAG,"===================")
                }else{
                    streetViewNearByCallBack.onLoading(false)
                    streetViewNearByCallBack.onFailure(response.errorBody()!!.toString())
                    Log.d(TAG,"==================="+response.errorBody()!!.toString())

                }
            }

            override fun onFailure(call: Call<StreetViewNearPlacesModel>, t: Throwable) {
                streetViewNearByCallBack.onLoading(false)
                streetViewNearByCallBack.onFailure(t.message!!)
                Log.d(TAG,"===========onFailure========")

            }

        })
    }


}